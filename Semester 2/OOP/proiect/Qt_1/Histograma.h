#pragma once
#include<qwidget.h>
#include"basket.h"
#include"Observer.h"
#include<qpainter.h>
class HistogramGUI :public QWidget,public Observer,public Observable {
private:
	Basket& basket;
public:
	HistogramGUI(Basket& basket):basket(basket){
		addObservcer(this);
	}

	void update() override {
		repaint();
	}

	void paintEvent(QPaintEvent* ev)override {
		QPainter p{ this };
		QPixmap background("fundal.jpg");  
		p.drawPixmap(rect(), background);
		p.setBrush(QColor(251,252,213));
		p.setPen(QColor(0, 0,0));
		qreal x = 10;
		for (const auto& prod : basket.getall()) {
			QRectF rectangle{ x,20.0,40.0,prod.getprice()*5.0 };

			p.drawEllipse(rectangle);
			x += 50;
		}
	}
};