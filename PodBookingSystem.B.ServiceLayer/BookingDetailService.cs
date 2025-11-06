using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer.UnitOfWorks;

namespace PodBookingSystem.B.ServiceLayer
{
    public class BookingDetailService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ILogger<AccountService> _logger;
        private readonly IConfiguration _configuration;
        public BookingDetailService(IUnitOfWork unitOfWork, ILogger<AccountService> logger, IConfiguration configuration)
        {
            _unitOfWork = unitOfWork;
            _logger = logger;
            _configuration = configuration;
        }
        public async Task<List<BookingDetail>> GetAllAsync()
        {
            try
            {
                return await _unitOfWork.BookingDetailRepository.GetBookingDetailsAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi lấy danh sách booking");
                throw new Exception("Không thể lấy danh sách booking. Vui lòng thử lại sau.", ex);
            }
            return new List<BookingDetail>();
        }
        public async Task<BookingDetail> GetByIdAsync(int id)
        {
            try
            {
                return await _unitOfWork.BookingDetailRepository.GetByIdAsync(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi lấy danh sách booking");
                throw new Exception("Không thể lấy danh sách booking. Vui lòng thử lại sau.", ex);
            }
            return new BookingDetail();
        }




        
        public async Task<int> Create(int bookingId, int roomId, CreateDetailDto dto)
        {
            try
            {
                var room = await _unitOfWork.RoomRepository.GetRoomAsync(roomId)
                    ?? throw new Exception("Phòng không tồn tại");

                var booking = await _unitOfWork.BookingRepository.GetByIdForUpdateAsync(bookingId)
                    ?? throw new Exception("Booking không tồn tại");

                // check slot trung
                foreach (var s in dto.Slots)
                {
                    var exists = await _unitOfWork.RoomSlotRepository.AnyAsync(rs =>
                        rs.RoomId == roomId &&
                        rs.SlotId == s.SlotId &&
                        rs.BookingDate == s.BookingDate &&
                        rs.BookingId != null);

                    if (exists)
                    {
                        string errorMsg = dto.BookingType == "DAY"
                            ? $"Phòng đã được đặt một phần vào ngày {s.BookingDate}. Không thể đặt cả ngày!"
                            : $"Slot {s.SlotId} ngày {s.BookingDate} đã được đặt!";

                        throw new Exception(errorMsg);
                    }
                }
                decimal totalPrice = dto.BookingType == "DAY"
                    ? (room.PriceDay ?? 0)
                    : (room.Price ?? 0) * dto.Slots.Count;

                var newDetail = new BookingDetail
                {
                    BookingId = bookingId,
                    RoomId = roomId,
                    StartTime = dto.StartTime,
                    EndTime = dto.EndTime,
                    BookingType = dto.BookingType,
                    TotalPrice = totalPrice,
                    Timestamp = DateTime.Now
                };

                booking.Total += totalPrice;

                // 5. LƯU DETAIL TRƯỚC ĐỂ CÓ ID
                await _unitOfWork.BookingDetailRepository.CreateAsync(newDetail);
                foreach (var s in dto.Slots)
                {
                    var roomSlot = new RoomSlot
                    {
                        RoomId = roomId,
                        SlotId = s.SlotId,
                        BookingId = bookingId,
                        BookingDate = s.BookingDate
                    };
                    await _unitOfWork.RoomSlotRepository.CreateAsync(roomSlot);
                }
                await _unitOfWork.BookingRepository.UpdateAsync(booking);

                return newDetail.BookingDetailId;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi tạo BookingDetail");
                throw new Exception("Không thể đặt phòng: " + ex.Message);
            }
        }

        // tru tien cu & cong tien moi & cap nhat slot
        public async Task<int> Update(int detailId, UpdateDetailDto dto)
        {
            try
            {
                var detail = await _unitOfWork.BookingDetailRepository.GetByIdAsync(detailId)
                    ?? throw new Exception("Detail không tồn tại");

                var room = await _unitOfWork.RoomRepository.GetRoomAsync(detail.RoomId)
                    ?? throw new Exception("Phòng không tồn tại");

                var booking = await _unitOfWork.BookingRepository.GetByIdAsync(detail.BookingId)
                    ?? throw new Exception("Booking không tồn tại");

                //tru tien cu
                booking.Total -= detail.TotalPrice;

                //xoa slot cu
                await _unitOfWork.RoomSlotRepository.DeleteByBookingDetailId(detailId);

                //check slot moi
                foreach (var s in dto.Slots)
                {
                    var exists = await _unitOfWork.RoomSlotRepository.AnyAsync(rs =>
                        rs.RoomId == detail.RoomId &&
                        rs.SlotId == s.SlotId &&
                        rs.BookingDate == s.BookingDate &&
                        rs.BookingId != booking.BookingId);

                    if (exists)
                        throw new Exception($"Slot {s.SlotId} đã được đặt!");
                }

                decimal newPrice = dto.BookingType == "DAY"
                    ? (room.PriceDay ?? 0)
                    : (room.Price ?? 0) * dto.Slots.Count;

                detail.StartTime = dto.StartTime;
                detail.EndTime = dto.EndTime;
                detail.BookingType = dto.BookingType;
                detail.TotalPrice = newPrice;


                booking.Total += newPrice;

                foreach (var s in dto.Slots)
                {
                    var roomSlot = new RoomSlot
                    {
                        RoomId = detail.RoomId,
                        SlotId = s.SlotId,
                        BookingId = detail.BookingId,
                        BookingDate = s.BookingDate
                    };
                    await _unitOfWork.RoomSlotRepository.CreateAsync(roomSlot);
                }

                await _unitOfWork.BookingDetailRepository.UpdateAsync(detail);
                await _unitOfWork.BookingRepository.UpdateAsync(booking);

                return detailId;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi cập nhật BookingDetail");
                throw;
            }
        }

        // tru tien + xoa slot
        public async Task<bool> Delete(int detailId)
        {
            try
            {
                var detail = await _unitOfWork.BookingDetailRepository.GetByIdAsyncForUpdate(detailId)
                    ?? throw new Exception("Không tìm thấy");

                var booking = await _unitOfWork.BookingRepository.GetByIdForUpdateAsync(detail.BookingId)
                    ?? throw new Exception("Booking không tồn tại");
                booking.Total -= detail.TotalPrice;
                await _unitOfWork.RoomSlotRepository.DeleteByBookingDetailId(detailId);

                var deleted = await _unitOfWork.BookingDetailRepository.DeleteAsync(detail);
                if (deleted)
                {
                    await _unitOfWork.BookingRepository.UpdateAsync(booking);

                }
                return deleted;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi xóa");
                throw;
            }
        }

    
    }
}
