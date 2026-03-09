package com.fitlife.service;

import com.fitlife.dto.MemberCreationRequest;
import com.fitlife.dto.MemberResponse;
import com.fitlife.dto.PageResponse;
import com.fitlife.entity.Member;
import com.fitlife.entity.User;
import com.fitlife.repository.MemberRepository;
import com.fitlife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    // --- 1. HÀM CŨ: TẠO HỘI VIÊN ---
    @Transactional
    public MemberResponse createMember(MemberCreationRequest request) {
        // Validate if phone number already exists
        if (memberRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered.");
        }

        // Fetch the User entity from DB
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        // Map DTO to Entity
        Member newMember = Member.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status("ACTIVE")
                .build();

        // Save to Database
        Member savedMember = memberRepository.save(newMember);

        // Map Entity back to Response DTO
        return MemberResponse.builder()
                .id(savedMember.getId())
                .userId(savedMember.getUser().getId())
                .fullName(savedMember.getFullName())
                .phone(savedMember.getPhone())
                .email(savedMember.getEmail())
                .status(savedMember.getStatus())
                .build();
    }

    // --- 2. HÀM CŨ: CẬP NHẬT ẢNH ĐẠI DIỆN ---
    @Transactional
    public String updateAvatar(String username, MultipartFile file) throws IOException {
        // FIX LỖI ĐỎ 1: Dùng userRepository để lấy User (chắc chắn có hàm này trả về Optional)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User"));

        Member member = user.getMember();
        if (member == null) {
            throw new RuntimeException("Không tìm thấy hội viên");
        }

        // Đẩy ảnh lên Cloudinary (Chỉ định folder lưu trữ là 'avatars')
        String avatarUrl = cloudinaryService.uploadImage(file);

        // Cập nhật URL vào Database
        member.setAvatarUrl(avatarUrl);
        memberRepository.save(member);

        return avatarUrl;
    }

    // --- 3. HÀM MỚI: PHÂN TRANG & LỌC DANH SÁCH HỘI VIÊN DÀNH CHO ADMIN ---
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getAllMembers(int page, int size, String sortBy, String sortDir, String keyword) {

        // Cấu hình Sắp xếp (Tăng dần / Giảm dần)
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Cấu hình Phân trang (Lưu ý: Spring Data JPA đánh số trang từ 0, nên FE truyền lên 1 thì BE phải trừ 1)
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Khai báo đối tượng Page để chứa kết quả từ Database
        Page<Member> memberPage;

        // Kiểm tra xem Admin có truyền từ khóa tìm kiếm (keyword) không?
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có tìm kiếm thì gọi hàm tìm kiếm tương đối (LIKE %keyword%)
            memberPage = memberRepository.findByFullNameContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            // Nếu không tìm kiếm gì thì lấy ra toàn bộ
            memberPage = memberRepository.findAll(pageable);
        }

        // Chuyển đổi dữ liệu từ List<Member> (Entity) sang List<MemberResponse> (DTO)
        List<MemberResponse> content = memberPage.getContent().stream()
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .userId(member.getUser() != null ? member.getUser().getId() : null)
                        .fullName(member.getFullName())
                        .email(member.getEmail())
                        .phone(member.getPhone())
                        .status(member.getStatus())
                        .avatarUrl(member.getAvatarUrl())
                        .build())
                .toList();

        // Đóng gói tất cả vào trong hộp PageResponse trả về cho Client
        return PageResponse.<MemberResponse>builder()
                .currentPage(page)
                .totalPages(memberPage.getTotalPages())
                .pageSize(memberPage.getSize())
                .totalElements(memberPage.getTotalElements())
                .data(content)
                .build();
    }
}