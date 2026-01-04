#pragma once
#include <exception>

template <typename ELement>
class MyIterator;

template <typename Element>
class DynamicVector {
private:
	int lg;
	int cp;
	Element* elements;
public:
	//constructor
	DynamicVector() noexcept;
	DynamicVector(const DynamicVector& ot);


	//distructor
	~DynamicVector();

	//Verifica daca capacitatea nu a trecut limita
	void ensure_cap() noexcept;

	//Functia adauga un element la finalul listei
	void push_back(const Element& elm);

	//Retutneaza True daca vectorul e null
	bool empty()const;

	//Returneaza nr de elemente din container
	int size()const noexcept;


	//suprascriem egalul
	 void operator=(const DynamicVector& ot);


	//face o copie elementului actual
	void copy(DynamicVector<Element>& ot);

	//Suprascriem operatorul []
	Element& operator[](int poz);

	//Functia sterge un element de o pozitie data
	void erase(const int& pos);


	//Functia returneaza un pointer la sfarsitul zonei de memorie
	MyIterator<Element> begin() const;

	//Functia returneaza un pointer la sfarsitul zonei de memorie
	MyIterator<Element> end() const;

	friend class MyIterator<Element>;

};

template <typename Element>
class MyIterator {

private:
	const DynamicVector<Element>& v;
	int poz = 0;
public:
	MyIterator(const DynamicVector<Element>& v);
	MyIterator(const DynamicVector<Element>& v, int poz);
	bool valid() const;
	Element& element() const;
	void next();
	Element& operator*();
	MyIterator& operator++();
	bool operator==(const MyIterator& other);
	bool operator!=(const MyIterator& other);
	MyIterator& operator+(int n);
};



template <typename Element>
MyIterator<Element>::MyIterator(const DynamicVector<Element>& v) : v{ v } {}

template <typename Element>
MyIterator<Element>::MyIterator(const DynamicVector<Element>& v, int poz) : v{ v }, poz{ poz } {}

template <typename ELement>
bool MyIterator<ELement>::valid() const {
	return poz < v.lg;
}

template <typename Element>
Element& MyIterator<Element>::element() const {
	return v.elems[poz];
}

template <typename Element>
void MyIterator<Element>::next() {
	poz++;
}

template <typename Element>
Element& MyIterator<Element>::operator*() {
	return element();
}

template <typename Element>
MyIterator<Element>& MyIterator<Element>::operator++() {
	next();
	return *this;
}

template <typename Element>
bool MyIterator<Element> :: operator==(const MyIterator& other) {
	return poz == other.poz;
}

template <typename Element>
bool MyIterator<Element> :: operator!=(const MyIterator& other) {
	return !(*this == other);
}


template <typename Element>
MyIterator<Element>& MyIterator<Element>::operator+(int n) {
	poz += n;
	return *this;
}


