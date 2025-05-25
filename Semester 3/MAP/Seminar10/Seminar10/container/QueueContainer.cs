using Task = Seminar10.domain.Task;

namespace Seminar10.container;

public class QueueContainer : AbstractContainer
{
    public override Task remove()
    {
        Task task = tasks[0];
        tasks.RemoveAt(0);
        return task;
    }
}