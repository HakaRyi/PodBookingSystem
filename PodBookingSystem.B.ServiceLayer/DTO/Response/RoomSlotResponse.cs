using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Response
{
    public class RoomSlotResponse
    {
        public int RoomId { get; set; }
        public int SlotId { get; set; }
        public int? BookingId { get; set; }
        public DateOnly? BookingDate { get; set; }
    }
}
