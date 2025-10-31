using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.C.RepositoryLayer
{
    public class FeedbackRepository
    {
        private readonly PodBookingSystemContext _context;

        public FeedbackRepository(PodBookingSystemContext context)
        {
            _context = context;
        }

        public async Task<List<Feedback>> GetAllFeedbacks()
        {
            return await _context.Feedbacks
                .Include(f => f.Booking)
                    .ThenInclude(b => b.User)
                .ToListAsync();
        }

        public async Task<Feedback?> GetFeedbackById(int id)
        {
            return await _context.Feedbacks
                .Include(f => f.Booking)
                    .ThenInclude(b => b.User)
                .FirstOrDefaultAsync(f => f.FeedbackId == id);
        }

        public Feedback GetByBooking(int id)
        {
            return _context.Feedbacks
                .Include(f => f.Booking)
                    .ThenInclude(b => b.User)
                .FirstOrDefault(f => f.BookingId == id);
        }

        public async Task<int> AddFeedback(Feedback feedback)
        {
            await _context.Feedbacks.AddAsync(feedback);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> UpdateFeedback(Feedback feedback)
        {
            _context.Feedbacks.Update(feedback);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> DeleteFeedback(int id)
        {
            var feedback = await _context.Feedbacks.FindAsync(id);
            if (feedback != null)
            {
                _context.Feedbacks.Remove(feedback);
                return await _context.SaveChangesAsync();
            }
            return 0;
        }
    }
}
