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
        FeedbackRepository FeedbackRepository { get; }
        BookingRepository BookingRepository { get; }
        BookingDetailRepository BookingDetailRepository { get; }
        RoomRepository RoomRepository { get; }

    }
    public class UnitOfWork : IUnitOfWork
    {
        private readonly PodBookingSystemContext _context;

        private AccountRepository accountRepository;
        private FeedbackRepository feedbackRepository;
        private BookingRepository bookingRepository;
        private BookingDetailRepository bookingDetailRepository;
        private RoomRepository roomRepository;

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

        public FeedbackRepository FeedbackRepository
        {
            get
            {
                return feedbackRepository ??= new FeedbackRepository(_context);
            }
        }
        public BookingRepository BookingRepository
        {
            get { return bookingRepository ??= new BookingRepository(_context); }

        }
        public BookingDetailRepository BookingDetailRepository
        {
            get { return bookingDetailRepository ??= new BookingDetailRepository(_context); }

        }
        public RoomRepository RoomRepository
        {
            get { return roomRepository ??= new RoomRepository(_context); }

        }


    }
}
