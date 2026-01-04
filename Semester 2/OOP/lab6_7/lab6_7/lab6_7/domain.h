#pragma once
#include <string>
#include <iostream>
using namespace std;
using std::string;

class Product
{
private:
	string name{ "" };
	string type{ "" };
	int price = 0;
	string productor{ "" };
public:
	Product() = default;
	Product(string name, string type, int price, string productor) : name{ name }, type{ type }, price{ price }, productor{ productor } {};
	Product(const Product& ot) :name{ ot.name }, type{ ot.type }, price{ ot.price }, productor{ ot.productor } {}
	
	~Product() = default;

		string getname() const;
		/*
		Transmite numele unui produs
		*/

		string gettype() const;
		/*
		Transmite tipul unui produs
		*/

		int getprice() const noexcept;
		/*
		Transmite pretul unui produs
		*/

		string getproductor() const;
		/*
		Transmite producatorul unui produs
		*/

		void setname(string name);
		/*
		Seteaza numele unui produs
		*/

		void settype(string type);
		/*
		Seteaza tipul unui produs
		*/

		void setprice(int price) noexcept;
		/*
		Seteaza pretul unui produs
		*/

		void setproductor(string productor);
		/*
		Seteaza numele producatorului
		*/

		bool operator==(const Product& other) const noexcept {
			return (other.name == this->name && other.type == this->type && other.productor == this->productor);
		}
		friend std::ostream& operator<<(std::ostream& stream, const Product& p);
};