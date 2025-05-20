import { SearchForm } from '@/components/SearchForm.tsx';

const SearchPage = () => {
    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Search Flights</h1>
            <SearchForm />
        </div>
    );
};

export default SearchPage;
