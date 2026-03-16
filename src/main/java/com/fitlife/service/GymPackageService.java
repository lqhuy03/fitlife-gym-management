package com.fitlife.service;

import com.fitlife.dto.GymPackageCreationRequest;
import com.fitlife.dto.GymPackageResponse;
import com.fitlife.dto.PageResponse;
import org.springframework.transaction.annotation.Transactional;

public interface GymPackageService {
    // Create Package
    GymPackageResponse createPackage(GymPackageCreationRequest request);

    // Get List Pagination
    PageResponse<GymPackageResponse> getAllPackages(int page, int size, String sortBy, String sortDir, String keyword);
}
