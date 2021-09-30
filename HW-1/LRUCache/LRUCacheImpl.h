#pragma once
#include "LRUCache.h"
#include <unordered_map>
#include <list>

template<class _Ty>
class LRUCacheImpl final:
    public LRUCache<_Ty> {

private:
    using value_type = _Ty;
    using my_base = LRUCache<value_type>;
    using return_type = my_base::return_type;
    

    template<class T>
    using map_iterator = typename std::unordered_map<value_type, T>::iterator;

    template<class T>
    using list_iterator = typename std::list<map_iterator<T>>::iterator;

    struct node_type {
        node_type(value_type&& aElem, list_iterator<node_type> aListIterator) :
            m_Item(aElem),
            m_iListIterator(aListIterator) {}

        value_type m_Item;
        list_iterator<node_type> m_iListIterator;
    };

    void push_into_list(map_iterator<node_type>& aIterator) {
        m_lList.push_front(aIterator);
        aIterator->second.m_iListIterator = m_lList.begin();
        return;
    }

    void raise_up(map_iterator<node_type>& aIterator) {
        m_lList.erase(aIterator->second.m_iListIterator);
        push_into_list(aIterator);
        return;
    }

protected:
    return_type _get(const value_type& aElem) override {
        if (auto it = m_mHashMap.find(aElem);
            it != m_mHashMap.end()) {
            raise_up(it);
            return { it->second.m_Item };
        }
        return {};
    }

    void _add(value_type&& aElem) override {
        auto it = m_mHashMap.insert(std::pair<value_type, node_type>(
                aElem,
                node_type(std::forward<value_type>(aElem), m_lList.end())
            )); 

        if (it.second) {
            push_into_list(it.first);

            if (m_lList.size() > my_base::m_nMaxSize) {
                m_mHashMap.erase(m_lList.back());
                m_lList.pop_back();
            }
        }
        else {
            raise_up(it.first);
        }
        return;
    }

    size_t _size() const override {
        return m_lList.size();
    }

public:
    LRUCacheImpl(size_t nSize) :
        LRUCache<value_type>(nSize),
        m_lList(),
        m_mHashMap(nSize) {}

private:
    std::unordered_map<value_type, node_type> m_mHashMap;
    std::list<map_iterator<node_type>> m_lList;
};
