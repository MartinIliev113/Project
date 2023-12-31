let currentPage = 0;
document.addEventListener('DOMContentLoaded', function () {
    fetchProducts(currentPage);
});

function fetchProducts(page) {
    let category = document.getElementById('category').value;
    let sort = document.getElementById('sort').value;
    let url = category ? `/products/${category}?page=${page}&sort=${sort}` : `/products?page=${page}&sort=${sort}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const products = data.content;

            const productContainer = document.getElementById('productCards');
            productContainer.innerHTML = '';

            products.forEach(product => {
                const cardColumn = document.createElement('div');
                cardColumn.className = 'col-md-4';
                cardColumn.style.marginBottom = '30px';

                const card = document.createElement('div');
                card.className = 'card h-100';
                card.style.width = '100%';
                card.style.marginBottom = '5rem';

                const cardImage = document.createElement('img');
                // product.primaryImageUrl =   product.primaryImageUrl;
                cardImage.src = product.primaryImageUrl;
                cardImage.className = 'card-img-top';
                cardImage.alt = 'Product Image';

                const cardBody = document.createElement('div');
                cardBody.className = 'card-body d-flex flex-column justify-content-between';

                const cardTitle = document.createElement('h5');
                cardTitle.className = 'card-title';
                cardTitle.textContent = product.title;

                cardTitle.style.textAlign = 'center';

                const cardText = document.createElement('p');
                cardText.className = 'card-text';
                cardText.textContent = product.description;


                cardText.style.maxHeight = '150px';
                cardText.style.overflow = 'hidden';
                cardText.style.textOverflow = 'ellipsis';


                const cardPrice = document.createElement('p');
                cardPrice.className = 'card-price';
                cardPrice.textContent = 'Price: $' + product.price.toFixed(2);

                const button = document.createElement('a');
                button.href = '/products/details/' + product.id;
                button.className = 'btn btn-primary btn-sm';
                button.textContent = 'Details';

                cardBody.appendChild(cardTitle);
                cardBody.appendChild(cardImage);
                cardBody.appendChild(cardText);
                cardBody.appendChild(cardPrice);
                cardBody.appendChild(button);


                card.appendChild(cardBody);
                cardColumn.appendChild(card);
                productContainer.appendChild(cardColumn);
                productContainer.appendChild(document.createElement("br"));
            });

            updateTitle(category);


            const prevPage = document.getElementById('prevPage');
            const nextPage = document.getElementById('nextPage');

            prevPage.classList.toggle('disabled', data.first);
            nextPage.classList.toggle('disabled', data.last);

            currentPage = data.number;
        })
        .catch(error => console.error('Error fetching products:', error));
}

function updateTitle(category) {
    const titleElement = document.getElementById('listingsTitle');
    if (category) {
        titleElement.textContent = category.toUpperCase() + ' LISTINGS';
    } else {
        titleElement.textContent = 'ALL LISTINGS';
    }
}