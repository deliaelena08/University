#include <iostream>
#include <fstream>
#include <string>
#include <chrono>
#include <algorithm>

using namespace std;

int* readBigIntFromFile(const string& path, int& numDigits) {
    ifstream file(path);
    if (!file.is_open()) {
        cerr << "Error opening file: " << path << endl;
        numDigits = 0;
        return nullptr;
    }

    // Ignor BOM UTF-8 dacă exista
    char firstChar;
    file.get(firstChar);
    if ((unsigned char)firstChar == 0xEF) {
        file.ignore(2);
    } else {
        file.unget();
    }

    if (!(file >> numDigits)) {
        cerr << "Error reading number of digits from file: " << path << endl;
        numDigits = 0;
        return nullptr;
    }

    if (numDigits <= 0) {
        cerr << "Invalid number of digits (" << numDigits << ") in file: " << path << endl;
        numDigits = 0;
        return nullptr;
    }

    int* digits = new int[numDigits];
    for (int i = numDigits - 1; i >= 0; --i) {
        char ch;
        if (!(file >> ch)) {
            cerr << "Not enough digits in file: " << path << endl;
            delete[] digits;
            numDigits = 0;
            return nullptr;
        }
        if (ch < '0' || ch > '9') {
            cerr << "Invalid character '" << ch << "' in file: " << path << endl;
            delete[] digits;
            numDigits = 0;
            return nullptr;
        }
        digits[i] = ch - '0';
    }

    return digits;
}

void saveBigInt(const string& path, int* arr, int size, int carry) {
    ofstream file(path);
    if (!file.is_open()) {
        cerr << "Error writing file: " << path << endl;
        return;
    }
    if (carry) file << carry;
    for (int i = size - 1; i >= 0; i--) {
        file << arr[i];
    }
    file << endl;
}

void printBigInt(int* arr, int size, bool reverse = true, int carry = 0) {
    if (reverse) {
        if (carry) cout << carry;
        for (int i = size - 1; i >= 0; --i) {
            cout << arr[i];
        }
    } else {
        if (carry) cout << carry;
        for (int i = 0; i < size; ++i) {
            cout << arr[i];
        }
    }
    cout << endl;
}

int main2(int argc, char* argv[]) {
    if (argc < 4) {
        cerr << "Usage: " << argv[0] << " <file1> <file2> <outputFile>" << endl;
        return 1;
    }

    string file1 = "./inputs/" + string(argv[1]);
    string file2 = "./inputs/" + string(argv[2]);
    string outputFile = "./outputs/" + string(argv[3]);

    int digits1 = 0, digits2 = 0;

    auto start = chrono::high_resolution_clock::now();

    int* num1 = readBigIntFromFile(file1, digits1);
    int* num2 = readBigIntFromFile(file2, digits2);

    if (!num1 || !num2) {
        if (!num1) cerr << "Error reading first number" << endl;
        if (!num2) cerr << "Error reading second number" << endl;
        delete[] num1;
        delete[] num2;
        return 1;
    }

    cout << "Digits 1: " << digits1 << endl;
    cout << "Digits 2: " << digits2 << endl;

    int resultSize = max(digits1, digits2);
    int* sumArr = new int[resultSize + 1]();

    int carry = 0;
    for (int i = 0; i < resultSize; ++i) {
        int temp = carry;
        if (i < digits1) temp += num1[i];
        if (i < digits2) temp += num2[i];
        sumArr[i] = temp % 10;
        carry = temp / 10;
    }
    if (carry) sumArr[resultSize] = carry;

    auto end = chrono::high_resolution_clock::now();
    double elapsed = chrono::duration<double, milli>(end - start).count();
    cout << "Elapsed time: " << elapsed << " ms" << endl;

    cout << "Number 1: ";
    printBigInt(num1, digits1);
    cout << "Number 2: ";
    printBigInt(num2, digits2);
    cout << "Sum: ";
    printBigInt(sumArr, carry ? resultSize + 1 : resultSize);

    saveBigInt(outputFile, sumArr, carry ? resultSize + 1 : resultSize, 0);

    delete[] num1;
    delete[] num2;
    delete[] sumArr;

    return 0;
}
