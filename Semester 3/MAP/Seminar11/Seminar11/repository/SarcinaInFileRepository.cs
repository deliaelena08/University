using Seminar11.Domain;

namespace Seminar11.repository;

internal class SarcinaInFileRepository : InFileRepository<string, Sarcina>
{
    public SarcinaInFileRepository(string fileName) : base(fileName, EntityToFileMapping.CreateSarcina)
    {
    }
}