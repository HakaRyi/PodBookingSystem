using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Request
{
    public class CreatePaymentRequest
    {
        public short OrderId { get; set; }
        public decimal Amount { get; set; }
        public string ReturnUrl { get; set; }
        public string CancelUrl { get; set; }
    }
}
