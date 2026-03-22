export const CITIES = ['Warszawa', 'Kraków', 'Łódź', 'Wrocław', 'Poznań', 'Gdańsk', 'Szczecin', 'Mielec', 'Rzeszów', 'Lublin', 'Białystok', 'Gdynia', 'Sopot', 'Katowice', 'Gliwice', 'Bydgoszcz', 'Toruń', 'Olsztyn', 'Opole', 'Zielona Góra', 'Koszalin', 'Kielce', 'Częstochowa', 'Radom', 'Sosnowiec', 'Tychy', 'Rybnik', 'Dąbrowa Górnicza', 'Elbląg', 'Płock', 'Wałbrzych', 'Włocławek', 'Zabrze', 'Bytom', 'Bielsko-Biała', 'Ruda Śląska'].sort((a, b) => a.localeCompare(b, "pl"));
export const CITY_OPTIONS = CITIES.map(city => ({ value: city, label: city }));

export const POPULAR_KEYWORDS = ['IT', 'Marketing', 'Sales', 'Finance', 'Health', 'Education', 'E-commerce', 'Tech', 'Design', 'Food', 'Travel', 'Real Estate', 'Automotive', 'Fashion', 'Sports', 'Entertainment', 'Technology', 'Retail', 'Food & Beverage', 'Travel & Hospitality', 'Fashion & Beauty', 'Sports & Fitness', 'Entertainment & Media', 'Finance & Insurance', 'Education & E-learning', 'Health & Wellness', 'Technology & Gadgets', 'Retail & E-commerce', 'Travel & Tourism', 'Real Estate & Property', 'Automotive & Vehicles', 'Fashion & Apparel', 'Sports & Recreation', 'Entertainment & Events', 'Finance & Banking', 'Education & Training', 'Medical', 'Technology & Software', 'Retail & Consumer Goods'].sort((a, b) => a.localeCompare(b, "en"));
export const KEYWORD_OPTIONS = POPULAR_KEYWORDS.map(kw => ({ value: kw, label: kw }));

export const customSelectStyles = {
    control: (base) => ({
        ...base,
        backgroundColor: 'var(--input-bg)',
        borderColor: 'var(--input-border)',
        color: 'white'
    }),
    menu: (base) => ({...base, backgroundColor: 'var(--input-bg)'}),
    option: (base, state) => ({
        ...base,
        backgroundColor: state.isFocused ? 'var(--emerald-hover)' : 'transparent',
        color: 'white',
        cursor: 'pointer'
    }),
    multiValue: (base) => ({...base, backgroundColor: 'var(--emerald-primary)'}),
    multiValueLabel: (base) => ({...base, color: 'white'}),
    singleValue: (base) => ({...base, color: 'white'}),
    input: (base) => ({...base, color: 'white'})
};