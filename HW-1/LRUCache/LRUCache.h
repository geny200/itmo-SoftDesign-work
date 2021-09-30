#pragma once
#include <cassert>
#include <algorithm>
#include <optional>

template<class _Ty>
class LRUCache {
protected:
	using value_type = _Ty;
	using return_type = std::optional<std::reference_wrapper<value_type>>;

private:
	virtual return_type _get(const value_type&) = 0;
	virtual void _add(value_type&&) = 0;
	virtual size_t _size() const = 0;

public:
	LRUCache(size_t nSize) :
		m_nMaxSize(nSize) {}

	inline size_t size() const {
		_ASSERT_EXPR(_size() <= m_nMaxSize, "The size is too big.");
		return _size();
	}

	inline void add(value_type&& value) {
#ifdef _DEBUG
		size_t nSizeBeforeAdd = size();
		auto valueBeforeAdd = _get(value);
		auto copyValue = value;
#endif // _DEBUG

		_add(std::forward<value_type>(value));

		_ASSERT_EXPR(
			size() == nSizeBeforeAdd && valueBeforeAdd.has_value()
			|| size() == std::min(m_nMaxSize, nSizeBeforeAdd + 1) && !valueBeforeAdd.has_value(),
			"The size had to be increased or unchanged."
		);
		_ASSERT_EXPR(
			_get(copyValue).has_value(),
			"The element had to be inserted."
		);
		_ASSERT_EXPR(
			!(_get(value).value().get() < copyValue)
			&& !(copyValue < _get(value).value().get()),
			"The inserted element is not equal to the given."
		);
	}

	inline void add(value_type& value) {
		add(std::forward<value_type>(value));
	}

	inline return_type get(const value_type& value) {
#ifdef _DEBUG
		size_t nSizeBeforeGet = size();
#endif // _DEBUG

		auto result = _get(value);

#ifdef _DEBUG
		if (result.has_value()) {
			_ASSERT_EXPR(
				result.value().get() == value,
				"The found element is not equal to given."
			);
		}

		_ASSERT_EXPR(
			size() == nSizeBeforeGet,
			"The size can't change without insertion."
		);
		_ASSERT_EXPR(
			result == _get(value),
			"The get operation changes the search result."
		);
#endif // _DEBUG
		return result;
	}

protected:
	size_t m_nMaxSize;
};