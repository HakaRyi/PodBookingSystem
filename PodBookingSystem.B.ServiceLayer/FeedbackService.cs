using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class FeedbackService
    {
        private readonly IUnitOfWork _unitOfWork;

        public FeedbackService(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<List<Feedback>> GetAllFeedbacks()
        {
            return await _unitOfWork.FeedbackRepository.GetAllFeedbacks();
        }

        public async Task<Feedback?> GetFeedbackById(int id)
        {
            return await _unitOfWork.FeedbackRepository.GetFeedbackById(id);
        }

        public Feedback GetByBooking(int bookingId)
        {
            return _unitOfWork.FeedbackRepository.GetByBooking(bookingId);
        }

        public async Task<int> AddFeedback(Feedback feedback)
        {
            feedback.Timestamp = DateTime.UtcNow;
            return await _unitOfWork.FeedbackRepository.AddFeedback(feedback);
        }
        public async Task<int> UpdateFeedback(Feedback feedback)
        {
            return await _unitOfWork.FeedbackRepository.UpdateFeedback(feedback);
        }
        public async Task<int> DeleteFeedback(int id)
        {
            return await _unitOfWork.FeedbackRepository.DeleteFeedback(id);
        }
    }
}
