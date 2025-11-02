using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;

namespace PodBookingSystem.C.RepositoryLayer
{
    public class PaymentRepository
    {
        private readonly PodBookingSystemContext context;
        public PaymentRepository(PodBookingSystemContext context)
        {
            this.context = context;
        }
        public async Task<List<Payment>> GetAllPaymentAsync()
        {
            return await context.Payments
                .Include(r => r.Booking)
                .OrderByDescending(r => r.PaymentId)
                .ToListAsync();
        }
        public async Task<Payment> GetPaymentAsync(int id)
        {
            return await context.Payments
               .Include(r => r.Booking)
               .FirstOrDefaultAsync(r => r.PaymentId == id);
        }
        public async Task<int> CreateAsync(Payment payment)
        {
            context.Payments.Add(payment);
            return await context.SaveChangesAsync();
        }
        public async Task<int> UpdateAsync(Payment payment)
        {
            context.Payments.Update(payment);
            return await context.SaveChangesAsync();
        }
        public async Task<bool> DeleteAsync(int id)
        {
            var payment = await GetPaymentAsync(id);
            if (payment != null)
            {
                context.Payments.Remove(payment);
                await context.SaveChangesAsync();
                return true;
            }
            return false;

        }
    }
}
