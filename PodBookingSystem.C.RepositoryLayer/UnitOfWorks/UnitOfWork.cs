using PodBookingSystem.C.RepositoryLayer.DBContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.C.RepositoryLayer.UnitOfWorks
{
    public interface IUnitOfWork
    { 
        AccountRepository AccountRepository { get; }
    }
    public class UnitOfWork : IUnitOfWork
    {
        private readonly PodBookingSystemContext _context;

        private AccountRepository accountRepository;

        public UnitOfWork(PodBookingSystemContext context)
        {
            _context = context;
        }
        public AccountRepository AccountRepository
        {
            get
            {
                return accountRepository ??= new AccountRepository(_context);
            }
        }
    }
}
