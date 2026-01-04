namespace Seminar10;

public class Circle : Shape
{
    private double radius;

    public Circle(double radius)
    {
        this.radius = radius;
    }
    public double Radius
    {
        get { return radius; }
        set { radius = value; }
    }
    public double computeArea()
    {
        return Math.PI * radius * radius;
    }

    public override string ToString()
    {
        return "Circle with radius: " + radius;
    }
}