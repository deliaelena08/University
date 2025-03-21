#pragma once

template <typename TElem>
class MyIterator;


template <typename TElem>
class VectorStudent {

private:
	int cap;
	int lg;
	TElem* elems;

public:
	//constructor default
	VectorStudent() :cap{ 2 }, lg{ 0 }, elems{ new TElem[cap] } {}


	//destructor
	~VectorStudent();


	//construcotr de copiere
	VectorStudent(const VectorStudent& other) : cap(other.cap), lg(other.lg), elems(new TElem[cap]) {
		for (int i = 0; i < lg; i++) {
			elems[i] = other.elems[i];
		}
	}

	//operator de assignment
	void operator=(const VectorStudent& other);

	//adauga un element in vector
	void add(const TElem& element);

	TElem& get(int poz) const;

	//returneaza numerul de elemente ale vectorului (int)
	int size() const;

	//definiesc operatorul de indexare  - returneaza TElem de pe pozitia index
	TElem& operator[](int index);

	//definiesc operatorul de indexare constant - returneaza TElem de pe pozitia index
	const TElem& operator[](int index) const;


	//sterge elemetul din vector de pe pozitia index
	void erase(TElem* position);


	MyIterator<TElem> begin() const;

	MyIterator<TElem> end() const;


	/*VectorStudent(std::vector<Book> books) {
		for (const auto& b : books) {
			push_back(b);
		}
	}*/

	/*std::vector<Book> toStdVector() {
		std::vector<Book> rez;
		rez.push_back();
		return rez;
	}*/

	friend class MyIterator<TElem>;

};



template <typename TElem>
VectorStudent<TElem>::~VectorStudent() {
	delete[] elems;
}

template <typename TElem>
void VectorStudent<TElem>::add(const TElem& element) {
	if (cap == lg) {
		TElem* aux = new TElem[cap * 2];
		for (int i = 0; i < lg; i++) {
			aux[i] = elems[i];
		}
		delete[] elems;
		elems = aux;
		cap = cap * 2;
	}
	elems[lg++] = element;
}

template <typename TElem>
TElem& VectorStudent<TElem>::get(int poz) const {
	return elems[poz];
}

template <typename TElem>
int VectorStudent<TElem>::size() const {
	return lg;
}

template <typename TElem>
TElem& VectorStudent<TElem>::operator[](int index) {
	return elems[index];
}

template <typename TElem>
const TElem& VectorStudent<TElem>::operator[](int index) const {
	return elems[index];
}

template <typename TElem>
void VectorStudent<TElem>::erase(TElem* position) {
	if (position < elems || position >= elems + lg)
		return;
	int index = position - elems;

	for (int i = index; i < lg - 1; i++) {
		elems[i] = elems[i + 1];
	}
	lg--;
}

template <typename TElem>
void VectorStudent<TElem>::operator=(const VectorStudent& other) {
	if (this == &other) {
		return;
	}
	delete[] elems;
	cap = other.cap;
	lg = other.lg;
	elems = new TElem[cap];
	for (int i = 0; i < lg; i++) {
		elems[i] = other.elems[i];
	}
}

template <typename TElem>
MyIterator<TElem> VectorStudent<TElem>::begin() const {
	return MyIterator<TElem>(*this);
}

template <typename TElem>
MyIterator<TElem> VectorStudent<TElem>::end() const {
	return MyIterator<TElem>(*this, lg);
}




template <typename TElem>
class MyIterator {

private:
	const VectorStudent<TElem>& v;
	int poz = 0;
public:
	MyIterator(const VectorStudent<TElem>& v);
	MyIterator(const VectorStudent<TElem>& v, int poz);
	bool valid() const;
	TElem& element() const;
	void next();
	TElem& operator*();
	MyIterator& operator++();
	bool operator==(const MyIterator& other);
	bool operator!=(const MyIterator& other);
	MyIterator& operator+(int n);
};

template <typename TElem>
MyIterator<TElem>::MyIterator(const VectorStudent<TElem>& v) : v{v} {}

template <typename TElem>
MyIterator<TElem>::MyIterator(const VectorStudent<TElem>& v, int poz) : v{ v }, poz{ poz } {}

template <typename TElem>
bool MyIterator<TElem>::valid() const {
	return poz < v.lg;
}

template <typename TElem>
TElem& MyIterator<TElem>::element() const {
	return v.elems[poz];
}

template <typename TElem>
void MyIterator<TElem>::next() {
	poz++;
}

template <typename TElem>
TElem& MyIterator<TElem>::operator*() {
	return element();
}

template <typename TElem>
MyIterator<TElem>& MyIterator<TElem>::operator++() {
	next();
	return *this;
}

template <typename TElem>
bool MyIterator<TElem> :: operator==(const MyIterator& other) {
	return poz == other.poz;
}

template <typename TElem>
bool MyIterator<TElem> :: operator!=(const MyIterator& other) {
	return !(*this == other);
}


template <typename TElem>
MyIterator<TElem>& MyIterator<TElem>::operator+(int n) {
	poz += n;
	return *this;
}








