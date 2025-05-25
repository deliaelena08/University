using Task = Seminar10.domain.Task;

namespace Seminar10.container;

public class StackContainer : AbstractContainer
{
    public override Task remove()
    {
        Task task = tasks[tasks.Count - 1];
        tasks.RemoveAt(tasks.Count - 1);
        return task;
    }
}