#pragma once
#include "repository.h"

class UndoAction {
public:
	virtual void doUndo() = 0;
	//destructorul pentru undo
	virtual ~UndoAction() = default;
};

class UndoAdd : public UndoAction {
	Product product;
	RepoAbstract& rep;
public:
	UndoAdd(const Product& product, RepoAbstract& rep) : product{ product }, rep{ rep } {}
	void doUndo() override {
		rep.delete_product(product);
	}
};

class UndoDelete : public UndoAction {
	Product product;
	RepoAbstract& rep;
public:
	UndoDelete(const Product& product, RepoAbstract& rep) : product{ product }, rep{ rep } {}
	void doUndo() override {
		rep.store(product);
	}
};

class UndoModify : public UndoAction {
	Product productUnchanged;
	Product productChanged;
	RepoAbstract& rep;
public:
	UndoModify(const Product& productChanged, const Product& productUnchanged, RepoAbstract& rep) : productChanged{ productChanged }, productUnchanged{ productUnchanged }, rep{ rep } {}
	void doUndo() override {
		rep.modify_product(productChanged, productUnchanged.getname(), productUnchanged.gettype(), productUnchanged.getprice());
	}
};
