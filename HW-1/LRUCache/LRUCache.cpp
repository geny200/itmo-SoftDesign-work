#include <iostream>
#include "LRUCacheImpl.h"

#include <set>
#include <list>
#include <random>

template<int nMaxSize = 10, int nCountOfInsertions = 20, int nCountOfRepeat = 10>
void test_1() {
    std::mt19937 generator(1242);

    std::set<int> rValuesSet;
    std::list<int> rValuesList;

    LRUCacheImpl<int> rLRUCache(nMaxSize);


    for (size_t k = 0; k != nCountOfRepeat; ++k) {
        for (size_t i = 0; i != nCountOfInsertions; ++i) {
            int value = generator();

            rValuesSet.insert(value);
            rLRUCache.add(value);

            auto foundValue = std::find(rValuesList.begin(), rValuesList.end(), value);
            if (foundValue != rValuesList.end()) {
                rValuesList.erase(foundValue);
            }

            rValuesList.push_front(value);
            while (rValuesList.size() > nMaxSize) {
                rValuesSet.erase(rValuesList.back());
                rValuesList.pop_back();
            }

            assert(rLRUCache.size() == rValuesSet.size());
            assert(rLRUCache.get(value).has_value());
            assert(rLRUCache.get(value).value().get() == value);
        }

        for (auto& iValue : rValuesList) {
            auto value = rLRUCache.get(iValue);

            assert(rLRUCache.size() == rValuesSet.size());
            assert(value.has_value());
            assert(value.value().get() == value);
        }
    }

    for (auto& iValue : rValuesSet) {
        auto value = rLRUCache.get(iValue);

        assert(rLRUCache.size() == rValuesSet.size());
        assert(value.has_value());
        assert(value.value().get() == value);
    }

    std::cout << "Stress test with parameters (" 
        << nMaxSize  << ',' 
        << nCountOfInsertions << ',' 
        << nCountOfRepeat << ')'
        << " passed" << std::endl;
    return;
}

void test_0() {
    LRUCacheImpl<int> rLRUCache(5);

    assert(rLRUCache.size() == 0);
    assert(rLRUCache.get(42).has_value() == false);
    assert(rLRUCache.get(0).has_value() == false);

    rLRUCache.add(42);
    assert(rLRUCache.size() == 1);
    assert(rLRUCache.get(42).has_value() == true);
    assert(rLRUCache.get(1242).has_value() == false);

    rLRUCache.add(42);
    rLRUCache.add(42);
    rLRUCache.add(42);
    assert(rLRUCache.size() == 1);
    assert(rLRUCache.get(42).has_value() == true);
    assert(rLRUCache.get(822).has_value() == false);

    rLRUCache.add(1242);
    assert(rLRUCache.size() == 2);

    rLRUCache.add(0);
    rLRUCache.add(314);
    rLRUCache.add(666);
    assert(rLRUCache.size() == 5);
    assert(rLRUCache.get(42).has_value() == true);
    assert(rLRUCache.get(666).has_value() == true);
    assert(rLRUCache.get(0).has_value() == true);
    assert(rLRUCache.get(314).has_value() == true);
    assert(rLRUCache.get(1242).has_value() == true);
    assert(rLRUCache.get(822).has_value() == false);

    rLRUCache.add(264234);
    rLRUCache.add(264234);
    assert(rLRUCache.size() == 5);
    assert(rLRUCache.get(42).has_value() == false);
    assert(rLRUCache.get(0).has_value() == true);
    assert(rLRUCache.get(314).has_value() == true);
    assert(rLRUCache.get(1242).has_value() == true);
    assert(rLRUCache.get(264234).has_value() == true);
    assert(rLRUCache.get(666).has_value() == true);

    rLRUCache.add(8675309);
    assert(rLRUCache.get(0).has_value() == false);
    assert(rLRUCache.get(42).has_value() == false);
    assert(rLRUCache.get(8675309).has_value() == true);
    assert(rLRUCache.get(314).has_value() == true);
    assert(rLRUCache.get(1242).has_value() == true);
    assert(rLRUCache.get(264234).has_value() == true);
    assert(rLRUCache.get(666).has_value() == true);

    std::cout << "Unit test passed" << std::endl;

    return;
}

int main() {
    test_0();
    test_1();
    test_1<5, 100, 20>();
    test_1<1000, 2000, 20>();
    test_1<1, 100, 50>();
}
