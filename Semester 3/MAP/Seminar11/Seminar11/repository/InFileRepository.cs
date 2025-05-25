using Seminar11.Domain;


namespace Seminar11.repository;

public delegate E CreateEntity<E>(string line);

internal abstract class InFileRepository<ID, E> : InMemoryRepository<ID, E> where E : Entity<ID>
{
    protected string fileName;
    protected CreateEntity<E> createEntity;

    public InFileRepository(string fileName, CreateEntity<E> createEntity) : base()
    {
        this.fileName = fileName;
        this.createEntity = createEntity;
        if (createEntity != null)
            loadFromFile();
    }

    protected virtual void loadFromFile()
    {
        List<E> list = DataReader.ReadData(fileName, createEntity);
        list.ForEach(x => entities[x.ID] = x);
    }
}