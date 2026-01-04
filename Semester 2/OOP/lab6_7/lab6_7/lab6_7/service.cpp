#include "service.h"

void ServiceProduct::addProduct(string name, string type, int price, string productor) {
	Product p1{ name,type,price,productor };
	valid.valid(p1);
	repo.store(p1);
}

void ServiceProduct::deleteProduct(string name, string productor) {
	Product p1= repo.find(name,productor);
	repo.delete_product(p1);
}

const Product& ServiceProduct::find_product(string name, string productor) {
	return repo.find(name, productor);
}

void ServiceProduct::modifyProduct(string name, string productor, string new_name, string new_type, int price) {
	Product p = repo.find(name, productor);
	repo.modify_product(p, new_name, new_type, price);
}



const DynamicVector<Product> ServiceProduct::filtre_by_name(string name) {
	DynamicVector<Product> filtred_products;
	DynamicVector<Product> mylist = repo.getallproducts();
	for (int i = 0; i < mylist.size(); i++)
	{
		Product p = mylist[i];
		if (p.getname() == name)
			filtred_products.push_back(p);
	}
	return filtred_products;
}

const DynamicVector<Product> ServiceProduct::filtre_by_price(int price) {
	DynamicVector<Product> filtred_products;
	DynamicVector<Product> mylist = repo.getallproducts();
	for (int i = 0; i < mylist.size(); i++)
	{
		Product p = mylist[i];
		if (p.getprice()<=price)
			filtred_products.push_back(p);
	}
	return filtred_products;
}

const DynamicVector<Product> ServiceProduct::filtre_by_productor(string productor) {
	DynamicVector<Product> filtred_products1;
	DynamicVector<Product> mylist1 = repo.getallproducts();
	for (int i = 0; i < mylist1.size(); i++)
	{
		Product p1 = mylist1[i];
		if (p1.getproductor() == productor)
			filtred_products1.push_back(p1);
	}
	return filtred_products1;
}

const DynamicVector<Product>  ServiceProduct::sort_by_name() {
	DynamicVector<Product> mylist =repo.getallproducts();
	for (int i = 0; i < mylist.size()-1; i++)
		for (int j = i + 1; j < mylist.size(); j++)
			if(mylist[i].getname()>mylist[j].getname())
			{
				Product p = mylist[i];
				mylist[i] = mylist[j];
				mylist[j] = p;
			}
	return mylist;
}

const DynamicVector<Product> ServiceProduct::sort_by_price() {
	DynamicVector<Product> mylist2 = repo.getallproducts();
	for (int i = 0; i < mylist2.size() - 1; i++)
		for (int j = i + 1; j < mylist2.size(); j++)
			if (mylist2[i].getprice() > mylist2[j].getprice())
			{
				Product p1 = mylist2[i];
				mylist2[i] = mylist2[j];
				mylist2[j] = p1;
			}
	return mylist2;
}

const DynamicVector<Product>  ServiceProduct::sort_by_name_and_price() {
	DynamicVector<Product> mylist = repo.getallproducts();
	for (int i = 0; i < mylist.size() - 1; i++)
		for (int j = i + 1; j < mylist.size(); j++)
		{
			if ((mylist[i].getname() > mylist[j].getname()) or (mylist[i].getname()==mylist[j].getname() and mylist[i].getprice()> mylist[j].getprice()))
			{
				Product p = mylist[i];
				mylist[i] = mylist[j];
				mylist[j] = p;
			}
		}
	return mylist;
}