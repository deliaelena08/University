using Seminar10.container;
using Seminar10.factory;
using Seminar10.strategy;

namespace Seminar10;

using Task = Seminar10.domain.Task;

class Program
{
    public static List<Circle> CirclesWithRadiusGreaterThen(List<Circle> circles, double x)
    {
        return circles.Where(c => c.Radius > x).ToList();
    }
    
    static void Main(string[] args)
    {
        Circle circle = new Circle(10);
        Square square = new Square(5);
        
        Console.WriteLine("Circle area: " + circle.computeArea());
        
        Console.WriteLine("Square area: " + square.computeArea());
        
        List<Circle> circles = new List<Circle>();
        circles.Add(new Circle(5));
        circles.Add(new Circle(10));
        circles.Add(new Circle(15));
        circles.Add(new Circle(20));
        
        List<Square> squares = new List<Square>();
        squares.Add(new Square(5));
        squares.Add(new Square(10));
        squares.Add(new Square(15));
        squares.Add(new Square(20));

        foreach (Square sq in squares)
        {
            Console.WriteLine("Square side: " + sq.Side);
        }

        foreach (Circle c in circles)
        {
            Console.WriteLine("Circle radius: " + c.Radius);
        }
        
        Console.WriteLine("Circles with radius greater than x:");
        

        double x = double.Parse(Console.ReadLine() ?? "0");
        
        foreach (Circle c in CirclesWithRadiusGreaterThen(circles, x))
        {
            Console.WriteLine("Circle radius: " + c.Radius + " Circle area: " + c.computeArea());
        }
        
        /// TASK
        List<Task> tasks = new List<Task>();
        tasks.Add(new Task("1", "Task 1"));
        tasks.Add(new Task("2", "Task 2"));
        tasks.Add(new Task("3", "Task 3"));
        tasks.Add(new Task("4", "Task 4"));
        
        AbstractContainer queueContainer = new QueueContainer();
        
        foreach (Task task in tasks)
        {
            queueContainer.add(task);
        }
        
        Console.WriteLine("Tasks in container QUEUE:");
        while (!queueContainer.isEmpty())
        {
            Task task = queueContainer.remove();
            Console.WriteLine(task);
        }
        
        Console.WriteLine("Tasks in container STACK:");
        
        AbstractContainer stackContainer = new StackContainer();
        
        foreach (Task task in tasks)
        {
            stackContainer.add(task);
        }
        
        while (!stackContainer.isEmpty())
        {
            Task task = stackContainer.remove();
            Console.WriteLine(task);
        }
        
        /// FACTORY
        Console.WriteLine("Factory");

        Container queueContainer1 = TaskContainerFactory.getInstance().createContainer(ContainerStrategy.FIFO);
        
        Container stackContainer1 = TaskContainerFactory.getInstance().createContainer(ContainerStrategy.LIFO);
        
        foreach (Task task in tasks)
        {
            queueContainer1.add(task);
            stackContainer1.add(task);
        }
        
        Console.WriteLine("Tasks in container QUEUE:");
        
        while (!queueContainer1.isEmpty())
        {
            Task task = queueContainer1.remove();
            Console.WriteLine(task);
        }
        
        Console.WriteLine("Tasks in container STACK:");

        while (!stackContainer1.isEmpty())
        {
            Task task = stackContainer1.remove();
            Console.WriteLine(task);
        }

    }
}