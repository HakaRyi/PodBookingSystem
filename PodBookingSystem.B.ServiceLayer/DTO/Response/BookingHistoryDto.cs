using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Response
{
    public class BookingHistoryDto
    {
        public int BookingId { get; set; }
        public DateOnly BookingDate { get; set; }
        public decimal Total { get; set; }
        public string Status { get; set; }
        public string? CancelReason { get; set; }
        public DateTime? CancelDate { get; set; }

        public string? PaymentStatus { get; set; }
        public string? FeedbackComment { get; set; }

        public List<BookingDetailDto> Details { get; set; } = new();
    }
    public class BookingDetailDto
    {
        public int RoomId { get; set; }
        public string RoomName { get; set; }
        public string RoomType { get; set; }
        public TimeOnly StartTime { get; set; }
        public TimeOnly EndTime { get; set; }
        public decimal TotalPrice { get; set; }
    }
}
