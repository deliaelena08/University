using Seminar11.Domain;

namespace Seminar11.repository;

public interface IRepository<ID, E> where E : Entity<ID>
{
    IEnumerable<E> FindAll();
}
