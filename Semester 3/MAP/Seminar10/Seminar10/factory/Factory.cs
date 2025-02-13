using Seminar10.container;
using Seminar10.strategy;

namespace Seminar10.factory;

public interface Factory
{
    public Container createContainer(ContainerStrategy strategy);
}