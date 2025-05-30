package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Image;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.repository.ImageRepository;
import com.SpringBootJdk22.SpringBootJdk22.repository.TourRepository;
import com.SpringBootJdk22.SpringBootJdk22.specification.TourSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;

    // Retrieve all products from the database
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

    public List<Tour> findToursByName(String name) {
        return tourRepository.findByNameContainingIgnoreCase(name);
    }

    // Find tours by item category
    public List<Tour> findByItemCategory(ItemCategory itemCategory) {
        return tourRepository.findByItemCategory(itemCategory);
    }
    // Add a new product to the database
    public Tour addTour(Tour tour) {
        return tourRepository.save(tour);
    }

    // Update an existing product
    public Tour updateTour(@NotNull Tour tour) {
        Tour existingTour = tourRepository.findById(tour.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        tour.getId() + " does not exist."));

        // Update fields
        existingTour.setName(tour.getName());
        existingTour.setPrice(tour.getPrice());
        existingTour.setPerson(tour.getPerson());
        existingTour.setDiscountPercentage(tour.getDiscountPercentage());
        existingTour.setDescription(tour.getDescription());
        existingTour.setItemCategory(tour.getItemCategory());
        existingTour.setAvatar(tour.getAvatar());

        return tourRepository.save(existingTour);
    }

    // Delete a product by its id
    public void deleteTourById(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        tourRepository.deleteById(id);
    }

    // Add a new image
    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    // Delete an image by its id
    public void deleteImageById(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new IllegalStateException("Image with ID " + id + " does not exist.");
        }
        imageRepository.deleteById(id);
    }

    public List<Tour> findToursByCategory(Long categoryId) {
        return tourRepository.findByItemCategory_Id(categoryId);
    }


    public List<Tour> filterTours(Long priceMin, Long priceMax, LocalDate startDate,
                                  String categoryName, String itemCategoryName) {
        Specification<Tour> spec = Specification
                .where(TourSpecification.filterByPriceRange(priceMin, priceMax))
                .and(TourSpecification.filterByStartDate(startDate))
                .and(TourSpecification.filterByCategoryAndItemCategory(categoryName, itemCategoryName)); // Lọc kết hợp

        return tourRepository.findAll(spec);
    }



}
