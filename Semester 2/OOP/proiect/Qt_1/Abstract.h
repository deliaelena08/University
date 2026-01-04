#pragma once
#include<vector>
#include"service.h"
#include<qwidget.h>
#include<QAbstractListModel>

class ProductListModel :public QAbstractListModel {
private:
	vector<Product> products;
public:
	ProductListModel(QObject *parent=nullptr):QAbstractListModel(parent){}

	void setProducts(vector<Product>& produse) {
		beginResetModel();
		products = produse;
		endResetModel();
	}

	void clear() {
		beginResetModel();
		products.clear();
		endResetModel();
	}

	void addItem(const Product& prod) {
		beginInsertRows(QModelIndex(), rowCount(), rowCount());
		products.push_back(prod);
		endInsertRows();
	}

	int rowCount(const QModelIndex& parent=QModelIndex()) const override {
		if (parent.isValid())return 0;
		return products.size();
	}

	QVariant data(const QModelIndex& index, int role) const override {
		if (!index.isValid() || index.row() >= products.size())
			return QVariant();

		const Product& product = products[index.row()];

		if (role == Qt::DisplayRole) {
			// Concatenate all product details into a single string
			QString productDetails = QString::fromStdString(product.getname()) + " | " +
				QString::fromStdString(product.gettype()) + " | " +
				QString::number(product.getprice()) + " | " +
				QString::fromStdString(product.getproductor());
			return productDetails;
		}

		return QVariant();
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const override {
		if (role != Qt::DisplayRole) return QVariant();

		if (orientation == Qt::Horizontal) {
			switch (section) {
			case 0: return tr("Nume");
			case 1: return tr("Tip");
			case 2: return tr("Pret");
			case 3: return tr("Producator");
			default: return QVariant();
			}
		}

		return QVariant();
	}

};