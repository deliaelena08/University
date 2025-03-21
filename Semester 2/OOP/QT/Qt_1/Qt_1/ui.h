#pragma once
#include "service.h"

class UI {
private:
	ServiceProduct& service;
public:
	UI(ServiceProduct& service) noexcept :service{ service } {};
	UI(const UI& ot) = delete;
	void printmenu();
	void printmenufiltre();
	void printmenusort();
	void printmenucos();
	void uiadd();
	void uimodify();
	void uidelete();
	void uifiltrebyname();
	void uifiltrebyprice();
	void uifiltrebyproductor();
	void uisortbyname();
	void uisortbyprice();
	void uisortbynameandprice();
	void uiprintall(vector<Product>&);
	void uiprintlist();
	void uiaddDefault();
	void uifiltrerun();
	void uisortrun();
	void uibasketrun();
	void uiaddbasket();
	void uiemptybasket();
	void uigeneratebasket();
	void uiexportbasket();
	void uiundo();
	void run();
};