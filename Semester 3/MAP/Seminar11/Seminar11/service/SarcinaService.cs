using Seminar11.Domain;
using Seminar11.repository;

namespace Seminar11.service;

internal class SarcinaService
{
    private IRepository<string, Sarcina> repo;

    public SarcinaService(IRepository<string, Sarcina> repo)
    {
        this.repo = repo;
    }


    public List<Sarcina> FindAllSarcini()
    {
        return repo.FindAll().ToList();
    }

    public Dictionary<Dificultate, double> FindEstimatedHoursForSarcinaLINQ()
    {
        return repo.FindAll().GroupBy(sarcina=>sarcina.TipDificultate).
            ToDictionary(
                group => group.Key, 
                group => group.Average(sarcina=>sarcina.NrOreEstimate)
                );
    }

    public Dictionary<Dificultate, double> FindEstimatedHoursForSarcina()
    {
        List<Sarcina> sarcines = repo.FindAll().ToList();
        var res = from sarcina in sarcines
            group sarcina by sarcina.TipDificultate into g
            select new
            {
                Dificultate = g.Key,
                MedieOreEstimate = g.Average(s => s.NrOreEstimate)
            };

        return res.ToDictionary(
            item => item.Dificultate,
            item => item.MedieOreEstimate
        );
                
    }
}