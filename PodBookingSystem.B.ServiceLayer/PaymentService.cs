using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class PaymentService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public PaymentService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
        }
        public async Task<List<Payment>> GetAll()
        {
             return await _unitOfWork.PaymentRepository.GetAllPaymentAsync();
        }
        public async Task<Payment> GetById(int id)
        {
            return await _unitOfWork.PaymentRepository.GetPaymentAsync(id);
        }
        public async Task<bool> Delete(int id)
        {
            var payment = await GetById(id);
            if (payment != null)
            {
                await _unitOfWork.PaymentRepository.DeleteAsync(id);
                return true;
            }
            return false;
        }
    }
}
