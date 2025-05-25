#pragma once
#include "domain.h"
#include <vector>
#include <string>
using std::string;
using std::vector;

class ExceptionValidator {
	vector<string> errors_message;
public:
	ExceptionValidator(vector<string> errors_message) :errors_message{ errors_message } {};

	string getErrorMessages() {
		string msg = "";
		for (const string e : errors_message) {
			msg += e;
		}
		return msg;
	}
};

/*
Validarea produsului va lua in considerare urmatoarele principii:
Orice string : numele,produsului,alproducatorului sau tipul trebuie sa contina cel putin 2 litere
Pretul nu poate fi un numar negativ
*/

class ProductValidator {

public:
	void valid(const Product& p) {
		vector<string> errors;
		if (p.getname().length() < 2)
			errors.push_back("Numele prdusului trebuie sa contina cel putin 2 caractere\n");
		if (p.getprice() < 0)
			errors.push_back("Pretul nu poate fi negativ\n");
		if (p.getproductor().length() < 2)
			errors.push_back("Producatorul trebuie sa aiba cel putin 2 litee\n");
		if (p.gettype().length() < 2)
			errors.push_back("Tipul trebuie sa aiba cel putin 2 litere\n");
		if (errors.size() > 0)
			throw ExceptionValidator(errors);
	}

};