package com.fitlife.service;

import com.fitlife.dto.GymPackageCreationRequest;
import com.fitlife.dto.GymPackageResponse;
import com.fitlife.dto.PageResponse;
import com.fitlife.entity.GymPackage;
import com.fitlife.repository.GymPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GymPackageService {

    private final GymPackageRepository gymPackageRepository;

    // --- HÀM CŨ: TẠO GÓI TẬP ---
    @Transactional
    public GymPackageResponse createPackage(GymPackageCreationRequest request) {
        if (gymPackageRepository.existsByName(request.getName())) {
            throw new RuntimeException("Package name already exists: " + request.getName());
        }

        GymPackage newPackage = GymPackage.builder()
                .name(request.getName())
                .price(request.getPrice())
                .durationMonths(request.getDurationMonths())
                .description(request.getDescription())
                .status("ACTIVE")
                .build();

        GymPackage savedPackage = gymPackageRepository.save(newPackage);

        return mapToResponse(savedPackage); // Gọi hàm phụ trợ ở dưới
    }

    // --- HÀM MỚI: LẤY DANH SÁCH PHÂN TRANG ---
    @Transactional(readOnly = true)
    public PageResponse<GymPackageResponse> getAllPackages(int page, int size, String sortBy, String sortDir, String keyword) {

        // 1. Cấu hình Sắp xếp
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // 2. Cấu hình phân trang (trừ đi 1 vì JPA đếm từ 0)
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 3. Query Database
        Page<GymPackage> packagePage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            packagePage = gymPackageRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            packagePage = gymPackageRepository.findAll(pageable);
        }

        // 4. Chuyển Entity -> DTO
        List<GymPackageResponse> content = packagePage.getContent().stream()
                .map(this::mapToResponse) // Gọi hàm phụ trợ cho gọn
                .toList();

        // 5. Đóng gói
        return PageResponse.<GymPackageResponse>builder()
                .currentPage(page)
                .totalPages(packagePage.getTotalPages())
                .pageSize(packagePage.getSize())
                .totalElements(packagePage.getTotalElements())
                .data(content)
                .build();
    }

    // --- HÀM PHỤ TRỢ DÙNG CHUNG ---
    private GymPackageResponse mapToResponse(GymPackage pkg) {
        return GymPackageResponse.builder()
                .id(pkg.getId())
                .name(pkg.getName())
                .price(pkg.getPrice())
                .durationMonths(pkg.getDurationMonths())
                .description(pkg.getDescription())
                .status(pkg.getStatus())
                .thumbnailUrl(pkg.getThumbnailUrl())
                .build();
    }
}