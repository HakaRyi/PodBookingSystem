using FirebaseAdmin.Auth;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using PodBookingSystem.B.ServiceLayer;
using PodBookingSystem.B.ServiceLayer.Helpers;
using PodBookingSystem.C.RepositoryLayer.Models;

namespace PodBookingSystem.A.WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly AccountService _accountService;

        public AccountController(AccountService accountService)
        {
            _accountService = accountService;
        }

        // 📌 GET: api/account
        [HttpGet]
        public async Task<IActionResult> GetAllAccounts()
        {
            try
            {
                var accounts = await _accountService.GetAllAccountsAsync();
                return Ok(accounts);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        // 📌 GET: api/account/{id}
        [HttpGet("{id:int}")]
        public async Task<IActionResult> GetAccountById(int id)
        {
            try
            {
                var account = await _accountService.GetAccountByIdAsync(id);
                if (account == null)
                    return NotFound(new { message = $"Không tìm thấy tài khoản có ID = {id}" });

                return Ok(account);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        // 📌 GET: api/account/by-email/{email}
        [HttpGet("by-email/{email}")]
        public async Task<IActionResult> GetAccountByEmail(string email)
        {
            try
            {
                var account = await _accountService.GetAccountByEmailAsync(email);
                if (account == null)
                    return NotFound(new { message = $"Không tìm thấy tài khoản có email = {email}" });

                return Ok(account);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        // 📌 POST: api/account
        [HttpPost]
        public async Task<IActionResult> CreateAccount([FromBody]
                                                        string email,
                                                        string name,
                                                        string phone,
                                                        string avatarUrl)
        {
            var account = new Account
            {
                Email = email,
                Name = name,
                Phone = phone,
                AvatarUrl = avatarUrl,
                Password = "",
                RoleId = 1
            };
            try
            {
                if (account == null)
                    return BadRequest(new { message = "Dữ liệu tài khoản không hợp lệ." });

                var success = await _accountService.CreateAccountAsync(account);
                if (success)
                    return CreatedAtAction(nameof(GetAccountById), new { id = account.AccId }, account);

                return BadRequest(new { message = "Không thể tạo mới tài khoản." });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        [HttpPut("{id:int}")]
        public async Task<IActionResult> UpdateAccount(int id, [FromBody] Account account)
        {
            try
            {
                if (account == null || id != account.AccId)
                    return BadRequest(new { message = "Thông tin tài khoản không khớp." });

                var success = await _accountService.UpdateAccountAsync(account);
                if (success)
                    return Ok(new { message = "Cập nhật tài khoản thành công." });

                return NotFound(new { message = $"Không tìm thấy tài khoản có ID = {id}" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        [Authorize]
        [HttpPut("update")]
        public async Task<IActionResult> UpdateAccount([FromBody] Account account)
        {
            try
            {
                // Xác thực Firebase token từ header Authorization, Tao mệt 
                var decodedToken = await FirebaseTokenHelper.VerifyFirebaseTokenAsync(Request);
                var email = decodedToken.Claims.ContainsKey("email")
                ? decodedToken.Claims["email"].ToString()
                : null;

                if (string.IsNullOrEmpty(email))
                    return Unauthorized(new { message = "Không tìm thấy email trong token." });

                // Lấy thông tin người dùng trong DB theo email
                var user = await _accountService.GetAccountByEmailAsync(email);
                if (user == null)
                    return NotFound(new { message = "Không tìm thấy tài khoản tương ứng trong hệ thống." });

                // Gán ID từ user DB vào account cập nhật
                account.AccId = user.AccId;

                var success = await _accountService.UpdateAccountAsync(account);
                if (success)
                    return Ok(new { message = "Cập nhật tài khoản thành công." });

                return NotFound(new { message = $"Không tìm thấy tài khoản" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        // 📌 DELETE: api/account/{id}
        [HttpDelete("{id:int}")]
        public async Task<IActionResult> DeleteAccount(int id)
        {
            try
            {
                var success = await _accountService.DeleteAccountAsync(id);
                if (success)
                    return Ok(new { message = "Xóa tài khoản thành công." });

                return NotFound(new { message = $"Không tìm thấy tài khoản có ID = {id}" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = ex.Message });
            }
        }

        //[HttpPost("login")]
        //public async Task<IActionResult> Login([FromBody] LoginRequest request)
        //{
        //    var user = await _accountService.GetAccountByEmailAsync(request.Email);
        //    //if (user == null || user.Password != request.Password)
        //    //    return Unauthorized(new { message = "Sai email hoặc mật khẩu" });

        //    var token = _accountService.GenerateJwtToken(user);
        //    await _accountService.UpdateAccountAsync(user);

        //    return Ok(new
        //    {
        //        accessToken = token,
        //        expiresIn = 7200
        //    });
        //}
    }
}
