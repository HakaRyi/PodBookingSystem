using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class BookingService
    {
       
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public BookingService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
        }
        public async Task<List<Booking>> GetAllAsync()
        {
            try
            {
                return await _unitOfWork.BookingRepository.GetBookingsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi lấy danh sách booking");
                throw new Exception("Không thể lấy danh sách booking. Vui lòng thử lại sau.", ex);
            }
            return new List<Booking>();
        }
        public async Task<Booking> GetBookingAsync(int id)
        {
            try
            {
                return await _unitOfWork.BookingRepository.GetByIdAsync(id);
            }
            catch (Exception ex) {
                _logger.LogError(ex, "Lỗi khi lấy danh sách booking");
                throw new Exception("Không thể lấy danh sách booking. Vui lòng thử lại sau.", ex);
            }
            return new Booking();
        }
        public async Task<int> CreateAsync(Booking booking, int userId)
        {
            try
            {
                var newBooking = new Booking()
                {
                    UserId = userId,
                    BookingDate = DateOnly.FromDateTime(DateTime.Now),
                    Total = 0,
                    Status = "BOOKED"

                };
                await _unitOfWork.BookingRepository.CretaeAsync(newBooking);
                decimal total = 0;
                foreach (var detail in booking.BookingDetails)
                {
                    var room = await _unitOfWork.RoomRepository.GetRoomAsync(detail.RoomId);
                    if (room == null)
                    {
                        _logger.LogError("ko thay phong nay");
                        throw new Exception($"Phòng có ID {detail.RoomId} không tồn tại");
                    }
                    decimal price = 0;
                    if(detail.BookingType == "DAY")
                    {
                        price = room.PriceDay ?? 0;
                    }
                    else
                    {
                        price = room.Price ?? 0;
                    }
                        var newDetail = new BookingDetail
                        {
                            BookingId = newBooking.BookingId,
                            RoomId = detail.RoomId,
                            BookingType = detail.BookingType,
                            TotalPrice = price,
                            StartTime = detail.StartTime,
                            EndTime = detail.EndTime,
                            Timestamp = DateTime.Now
                        };
                    total += price;
                    await _unitOfWork.BookingDetailRepository.CretaeAsync(newDetail);
                   
                }
                newBooking.Total = total;
                await _unitOfWork.BookingRepository.UpdateAsync(newBooking);
                return 1;

            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi create booking");
                throw new Exception("Không thể create booking. Vui lòng thử lại sau.", ex);
            }
            return 0;
        }
        public async Task<int> UpdateAsync(int id,Booking booking)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.BookingDate = booking.BookingDate;
                exisitingBooking.Total = booking.Total;
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }
        public async Task<int> UpdateStatusBOOKEDAsync(int id)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "BOOKED";
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }
        public async Task<int> UpdateStatusCHECKINAsync(int id)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "CHECK-IN";
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }
        public async Task<int> UpdateStatusAVAILABLEAsync(int id)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "AVAILABLE";
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }
        public async Task<int> UpdateStatusCHECKOUTAsync(int id)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "CHECK-OUT";
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }
        public async Task<int> UpdateStatusDONEAsync(int id)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "DONE";
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return 1;
            }
            catch (Exception ex)
            {
            }
            return 0;
        }

        public async Task<(bool Success, Booking booking)> UpdateStatusCANCELEDAsync(int id, string reason)
        {
            try
            {
                var exisitingBooking = await GetBookingAsync(id);
                exisitingBooking.Status = "CANCELED";
                exisitingBooking.CancelReason = reason;
                exisitingBooking.CancelDate = DateTime.Now;
                await _unitOfWork.BookingRepository.UpdateAsync(exisitingBooking);
                return (true, exisitingBooking);
            }
            catch (Exception ex)
            {
                throw new Exception("Lỗi khi cập nhật trạng thái hủy đặt phòng", ex);
            }
        }
        public async Task<bool> DeleteAsync(int id)
        {
            try
            {
                return await _unitOfWork.BookingRepository.DeleteAsync(id);
            }
            catch (Exception ex)
            {
            }
            return false;
        }

    }
}
