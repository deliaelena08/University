namespace Seminar11.repository;

internal class DataReader
{
    public static List<T> ReadData<T>(string fileName, CreateEntity<T> createEntity)
    {
        List<T> list = new();
        using (var sr = new StreamReader(fileName))
        {
            string s;
            while ((s = sr.ReadLine()) != null)
            {
                var entity = createEntity(s);
                list.Add(entity);
            }
        }

        return list;
    }
}