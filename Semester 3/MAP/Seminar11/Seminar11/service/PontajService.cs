using Seminar11.Domain;
using Seminar11.repository;

namespace Seminar11.service;

internal class PontajService
{
    private IRepository<string, Pontaj> repo;

    public PontajService(IRepository<string, Pontaj> repo)
    {
        this.repo = repo;
    }

    public List<Pontaj> FindAllPontaje()
    {
        return repo.FindAll().ToList();
    }
    
    public List<(Angajat Angajat, double Venit)> GroupeByHarnicie()
    {
        var pontaje = repo.FindAll().ToList();

        var res = (from pontaj in pontaje
                group pontaj by pontaj.Angajat into grouped
                let venitTotal = grouped.Sum(p => p.Sarcina.NrOreEstimate * p.Angajat.VenitPeOra)
                orderby venitTotal descending
                select (grouped.Key, venitTotal))
            .Take(2);

        return res.ToList();
    }

    public List<(Angajat Angajat, double Venit)> GroupeByHarnicieLINQ()
    {
        return repo.FindAll()
            .GroupBy(pontaj => pontaj.Angajat)
            .Select(group => new
            {
                Angajat = group.Key,
                Venit = group.Sum(pontaj => pontaj.Sarcina.NrOreEstimate * pontaj.Angajat.VenitPeOra)
            })
            .OrderByDescending(x => x.Venit)
            .Take(2)
            .Select(x => (x.Angajat, x.Venit))
            .ToList();
    }
}