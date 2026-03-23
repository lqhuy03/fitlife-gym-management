package com.fitlife.service;

import com.fitlife.dto.GymPackageRequest;
import com.fitlife.dto.GymPackageResponse;
import com.fitlife.dto.PageResponse;

public interface GymPackageService {

    // Lấy danh sách có phân trang và tìm kiếm
    PageResponse<GymPackageResponse> getAllPackages(int page, int size, String sortBy, String sortDir, String keyword);

    // Lấy chi tiết 1 gói tập
    GymPackageResponse getPackageById(Long id);

    // Tạo gói tập mới
    default GymPackageResponse createPackage(GymPackageRequest request) {
        return null;
    }

    // Cập nhật thông tin gói tập
    GymPackageResponse updatePackage(Long id, GymPackageRequest request);

    // Xóa mềm (Đổi trạng thái ACTIVE/INACTIVE)
    void togglePackageStatus(Long id);
}