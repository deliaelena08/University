using Seminar11.Domain;

namespace Seminar11.repository;

internal class AngajatInFileRepository : InFileRepository<string, Angajat>
{
    public AngajatInFileRepository(string fileName) : base(fileName, EntityToFileMapping.CreateAngajat)
    {
    }
}