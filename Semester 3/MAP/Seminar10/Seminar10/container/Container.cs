using Task = Seminar10.domain.Task;

namespace Seminar10.container;

public interface Container
{
    Task remove();
    
    void add(Task task);
    
    bool isEmpty();
    
    int size();
}