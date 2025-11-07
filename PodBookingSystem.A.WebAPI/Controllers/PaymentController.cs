using Microsoft.AspNetCore.Mvc;
using Net.payOS.Types;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.DTO.Request;
using PodBookingSystem.C.RepositoryLayer.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        private readonly PaymentService _service;
        private readonly PayOsPaymentService _payOsService;
        public PaymentController(PaymentService service, PayOsPaymentService payOsService)
        {
            _service = service;
            _payOsService = payOsService;
        }
        // GET: api/<PaymentController>
        [HttpGet]
        public async Task<List<Payment>> Get()
        {
            return await _service.GetAll();
        }

        // GET api/<PaymentController>/5
        [HttpGet("{id}")]
        public async Task<Payment> Get(int id)
        {
            return await _service.GetById(id);
        }

        // POST api/<PaymentController>
        [HttpPost]
        public async Task<int> Post([FromBody] string value)
        {
            return 0;
        }

        // PUT api/<PaymentController>/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/<PaymentController>/5
        [HttpDelete("{id}")]
        public async Task<bool> Delete(int id)
        {
            return await _service.Delete(id);
        }

        [HttpPost("create")]
        public async Task<IActionResult> CreatePayment([FromBody] CreatePaymentRequest request)
        {
            var url = await _payOsService.CreatePaymentUrlAsync(request.OrderId, request.Amount, request.ReturnUrl, request.CancelUrl);
            return Ok(new { checkoutUrl = url });
        }

        [HttpPost("webhook")]
        public async Task<IActionResult> Webhook([FromBody] WebhookType webhookBody)
        {
            try
            {
                Console.WriteLine("Webhook received raw: " + System.Text.Json.JsonSerializer.Serialize(webhookBody));

                var data = await _payOsService.VerifyWebhook(webhookBody);

                if (data == null)
                {
                    Console.WriteLine("Webhook invalid or test ping, returning 200 OK anyway");

                    return Ok(new { success = true, message = "Webhook test or invalid data ignored" });
                }
                Console.WriteLine("Webhook valid - order updated");
                return Ok(new { success = true, message = "Webhook processed successfully" });
            }
            catch (Exception ex)
            {
                Console.WriteLine("Webhook error: " + ex.Message);
                return Ok(new { success = false, error = ex.Message });
            }
        }
    }
}
