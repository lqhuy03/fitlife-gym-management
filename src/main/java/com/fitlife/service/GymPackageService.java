package com.fitlife.service;

import com.fitlife.dto.GymPackageCreationRequest;
import com.fitlife.dto.GymPackageResponse;
import com.fitlife.dto.PageResponse;

public interface GymPackageService {

    // Lấy danh sách có phân trang và tìm kiếm
    PageResponse<GymPackageResponse> getAllPackages(int page, int size, String sortBy, String sortDir, String keyword);

    // Lấy chi tiết 1 gói tập
    GymPackageResponse getPackageById(Long id);

    // Tạo gói tập mới
    GymPackageResponse createPackage(GymPackageCreationRequest request);

    // Cập nhật thông tin gói tập
    GymPackageResponse updatePackage(Long id, GymPackageCreationRequest request);

    // Xóa mềm (Đổi trạng thái ACTIVE/INACTIVE)
    void togglePackageStatus(Long id);
}