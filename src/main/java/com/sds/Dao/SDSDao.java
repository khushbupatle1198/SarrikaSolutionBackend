package com.sds.Dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sds.Model.Banner;
import com.sds.Model.Blog;
import com.sds.Model.Consultation;
import com.sds.Model.ConsultationBooking;
import com.sds.Model.Course;
import com.sds.Model.CoursePurchase;
import com.sds.Model.EReport;
import com.sds.Model.EReportPurchase;
import com.sds.Model.Review;
import com.sds.Model.User;
import com.sds.Model.Video;

public interface SDSDao {

	void saveCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    void updateCourse(Course course);
    void deleteCourse(Long id);
    
 // Add to SDSservices interface
    EReport saveEReport(EReport eReport);
    List<EReport> getAllEReports();
    EReport getEReportById(Long id);
    EReport updateEReport(EReport eReport);
    void deleteEReport(Long id);
    
    // Consultation methods
    List<Consultation> getAllConsultations();
    Consultation getConsultationById(Long id);
    Consultation saveConsultation(Consultation consultation);
    Consultation updateConsultation(Consultation consultation);
    void deleteConsultation(Long id);
    
  //BANNER
    Banner saveBanner(Banner banner);
    List<Banner> getAllBanners();
    Banner getBannerById(Long id);
    Banner updateBanner(Banner banner);
    void deleteBanner(Long id);
    
 // BLOGS
    List<Blog> getAllBlogs();
    Blog getBlogById(Long id);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Blog blog);
    void deleteBlog(Long id);
    List<Blog> getFeaturedBlogs();
    List<Blog> getBlogsByCategory(String category);
    
 // Add these methods to your SDSDao interface
    Video saveVideo(Video video);
    List<Video> getVideosByCourseId(Long courseId);
    Video getVideoById(Long id);
    void deleteVideo(Long id);
    Video updateVideo(Video video);
    
    User findUserByEmail(String email);
    User saveUser(User user);
    User updateUser(User user);
    User getUserById(Long id);
    User findUserByResetToken(String token);
    
    CoursePurchase savePurchase(CoursePurchase purchase);
    CoursePurchase getPurchaseById(Long id);
    CoursePurchase updatePurchase(CoursePurchase purchase);
   
    List<CoursePurchase> getAllPurchases();
    List<CoursePurchase> getPurchasesByStatus(String status);
    List<CoursePurchase> getPurchasesByUser(Long userId);
    
    ConsultationBooking saveBooking(ConsultationBooking booking);
	List<ConsultationBooking> getBookingsByStatus(String status);
	List<ConsultationBooking> getAllBookings();
	ConsultationBooking getBookingById(Long id);
	void updateBooking(ConsultationBooking booking);
 
	EReportPurchase saveEReportPurchase(EReportPurchase purchase);
	List<EReportPurchase> getEReportPurchasesByStatus(String status);
	List<EReportPurchase> getAllEReportPurchases();
	EReportPurchase getEReportPurchaseById(Long id);
	void updateEReportPurchase(EReportPurchase purchase);
	
	Review saveReview(Review review);
	List<Review> getAllReviews();
	void deleteReview(Long id);
	
	List<User> getAllUsers();
	
	long getTotalUsers();
	long getTotalCoursePurchases();
	long getTotalConsultationBookings();
	long getTotalEReportPurchases();
	Double getRevenueForPeriod(String period);
	Map<String, Long> getCountsForPeriod(String period);
}
