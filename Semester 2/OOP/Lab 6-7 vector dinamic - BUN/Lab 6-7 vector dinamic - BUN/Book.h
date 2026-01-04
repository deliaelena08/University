#pragma once
#include <string>
#include <iostream>
using namespace std;

class Book {

private:
	string titlu{ "" };
	string autor{ "" };
	string gen{ "" };
	int an{ 0 };
public:
	Book() = default;
	Book(string titlu, string autor, string gen, int an) :titlu{ titlu }, autor{ autor }, gen{ gen }, an{ an } {};

	Book(const Book& ot) :titlu{ ot.titlu }, autor{ ot.autor }, gen{ ot.gen }, an{ ot.an } {
		cout << "[Book] Copy constructor apelat.\n";
	}

	string getTitlu() const;
	string getAutor() const;
	string getGen() const;
	int getAn() const;

	void setTitlu(string titluNou);
	void setAutor(string autorNou);
	void setGen(string genNou);
	void setAn(int anNou);

	bool operator==(const Book& other) const {
		return (other.titlu == this->titlu && other.autor == this->autor);
	}

	friend std::ostream& operator<<(std::ostream& stream, const Book& b);
};

void testeDomain();

