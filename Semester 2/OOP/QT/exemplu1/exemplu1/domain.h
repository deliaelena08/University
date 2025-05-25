#pragma once
#include<string>
#include<iostream>
using namespace std;
using std::string;
class Tractor {
private:
	int id = 0;
	string name{ "" };
	string type{ "" };
	int wheels = 0;
public:
	Tractor() = delete;
	Tractor(int id, string name, string type, int wheels) : id{ id }, name{ name }, type{ type }, wheels{ wheels } {};
	Tractor(const Tractor& ot) : id{ ot.id }, name{ ot.name }, type{ ot.type }, wheels{ ot.wheels } {}

	int getId() const;
	string getName() const;
	string getType() const;
	int getWheels() const;

	void setId(int id);
	void setName(string name);
	void setType(string type);
	void setWheels(int nr);
};