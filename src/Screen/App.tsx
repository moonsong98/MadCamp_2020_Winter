import React, { useState } from 'react';
import { BrowserRouter, Route, Link } from 'react-router-dom';
import Row from '../components/App/Row';
import PostDetail from './Post';
import WritePage from './WritePage';
import MediaCard from '../components/App/Card';
import PrimarySearchAppBar from '../components/App/AppBar';
import PostPrices from './PostPrices';
import PriceDetail from './PriceDetail';
import CreatePrice from './CreatePrice';

const preparedMovie: Movie[] = [
	{
		originalName: 'Chef',
		name: 'Chef',
		posterPath:
			'https://lh3.googleusercontent.com/proxy/t-uyLVyxMdk-ApRakZZ8uEd_gfTi8JoQiIEvYx4ElxeF1FzXBnp0nC_8EG-e8tKHcbhotAfoGrDLcNK9iu-sxYntS2czwq7epS3Ts8z7OQA50rGJlurQ-0ME4eqjL9wv5cYA9TPTH3zrZwEi47tzWOPyy2uQ1sjLWsstdZXDH83khNfSzV4',
	},
	{
		originalName: 'Coco',
		name: 'Coco',
		posterPath:
			'https://m.media-amazon.com/images/M/MV5BYjQ5NjM0Y2YtNjZkNC00ZDhkLWJjMWItN2QyNzFkMDE3ZjAxXkEyXkFqcGdeQXVyODIxMzk5NjA@._V1_.jpg',
	},
	{
		originalName: '범죄도시',
		name: '범죄도시',
		posterPath:
			'https://lh3.googleusercontent.com/proxy/sQ4gZrtGjoFI6eGU8e_XMR_5K5BxImQKfXeaD5m93hbLNn6EnxjzXqbfXLTZ9wSH32gjKhTGx8ed9BFlg_L2Pel9Z0Y',
	},
	{
		originalName: 'Eternal Sunshine',
		name: 'Eternal Sunshine',
		posterPath: 'https://miro.medium.com/max/3200/1*-liR3H_fmJemOp4qOUfpyA.jpeg',
	},
	{
		originalName: 'Fast & Furious 7',
		name: 'Fast & Furious 7',
		posterPath:
			'https://cdn.shopify.com/s/files/1/0969/9128/products/Fast_Furious_7_-_Paul_Walker_-_Vin_Diesel_-_Dwayne_Johnson_-_Hollywood_Action_Movie_Poster_dc9198b7-c29f-4725-910c-366cef8dc546.jpg?v=1582543223',
	},
	{
		originalName: 'Martian',
		name: 'Martian',
		posterPath: 'https://images-na.ssl-images-amazon.com/images/I/A1%2BFw58qbDL._AC_SL1500_.jpg',
	},
	{
		originalName: 'Midnight in Paris',
		name: 'Midnight in Paris',
		posterPath: 'https://m.media-amazon.com/images/M/MV5BMTM4NjY1MDQwMl5BMl5BanBnXkFtZTcwNTI3Njg3NA@@._V1_.jpg',
	},
	{
		originalName: 'Music and Lyrics',
		name: 'Music and Lyrics',
		posterPath:
			'https://www.pastposters.com/cw3/assets/product_full/R/music-and-lyrics-cinema-quad-movie-poster-(1).jpg',
	},
	{
		originalName: '기생충',
		name: '기생충',
		posterPath:
			'https://upload.wikimedia.org/wikipedia/ko/6/60/%EA%B8%B0%EC%83%9D%EC%B6%A9_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg',
	},
	{
		originalName: 'The Terminal',
		name: 'The Terminal',
		posterPath: 'https://images-na.ssl-images-amazon.com/images/I/91avFh9KUhL._SL1500_.jpg',
	},
	{
		originalName: 'Black Panther',
		name: 'Black Panther',
		posterPath: 'https://m.media-amazon.com/images/M/MV5BMTg1MTY2MjYzNV5BMl5BanBnXkFtZTgwMTc4NTMwNDI@._V1_.jpg',
	},
	{
		originalName: '너의 결혼식',
		name: '너의 결혼식',
		posterPath: 'https://ojsfile.ohmynews.com/STD_IMG_FILE/2018/0826/IE002383442_STD.jpg',
	},
	{
		originalName: 'Begine Again',
		name: 'Begin Again',
		posterPath: 'https://www.movieviral.com/wp-content/uploads/2014/06/begin-again-poster.jpg',
	},
];
const dummyCards: Event[] = [
	{
		title: 'Chef',
		content: 'Chef',
		image:
			'https://lh3.googleusercontent.com/proxy/t-uyLVyxMdk-ApRakZZ8uEd_gfTi8JoQiIEvYx4ElxeF1FzXBnp0nC_8EG-e8tKHcbhotAfoGrDLcNK9iu-sxYntS2czwq7epS3Ts8z7OQA50rGJlurQ-0ME4eqjL9wv5cYA9TPTH3zrZwEi47tzWOPyy2uQ1sjLWsstdZXDH83khNfSzV4',
	},
	{
		title: 'Coco',
		content: 'Coco',
		image:
			'https://m.media-amazon.com/images/M/MV5BYjQ5NjM0Y2YtNjZkNC00ZDhkLWJjMWItN2QyNzFkMDE3ZjAxXkEyXkFqcGdeQXVyODIxMzk5NjA@._V1_.jpg',
	},
	{
		title: '범죄도시',
		content: '범죄도시',
		image:
			'https://lh3.googleusercontent.com/proxy/sQ4gZrtGjoFI6eGU8e_XMR_5K5BxImQKfXeaD5m93hbLNn6EnxjzXqbfXLTZ9wSH32gjKhTGx8ed9BFlg_L2Pel9Z0Y',
	},
	{
		title: 'Eternal Sunshine',
		content: 'Eternal Sunshine',
		image: 'https://miro.medium.com/max/3200/1*-liR3H_fmJemOp4qOUfpyA.jpeg',
	},
	{
		title: 'Fast & Furious 7',
		content: 'Fast & Furious 7',
		image:
			'https://cdn.shopify.com/s/files/1/0969/9128/products/Fast_Furious_7_-_Paul_Walker_-_Vin_Diesel_-_Dwayne_Johnson_-_Hollywood_Action_Movie_Poster_dc9198b7-c29f-4725-910c-366cef8dc546.jpg?v=1582543223',
	},
];

const dummyHomes: Event[] = [
	{
		title: 'Chef',
		content: 'Chef',
		image:
			'https://lh3.googleusercontent.com/proxy/t-uyLVyxMdk-ApRakZZ8uEd_gfTi8JoQiIEvYx4ElxeF1FzXBnp0nC_8EG-e8tKHcbhotAfoGrDLcNK9iu-sxYntS2czwq7epS3Ts8z7OQA50rGJlurQ-0ME4eqjL9wv5cYA9TPTH3zrZwEi47tzWOPyy2uQ1sjLWsstdZXDH83khNfSzV4',
	},
	{
		title: 'Coco',
		content: 'Coco',
		image:
			'https://m.media-amazon.com/images/M/MV5BYjQ5NjM0Y2YtNjZkNC00ZDhkLWJjMWItN2QyNzFkMDE3ZjAxXkEyXkFqcGdeQXVyODIxMzk5NjA@._V1_.jpg',
	},
	{
		title: '범죄도시',
		content: '범죄도시',
		image:
			'https://lh3.googleusercontent.com/proxy/sQ4gZrtGjoFI6eGU8e_XMR_5K5BxImQKfXeaD5m93hbLNn6EnxjzXqbfXLTZ9wSH32gjKhTGx8ed9BFlg_L2Pel9Z0Y',
	},
];

type Movie = { originalName: string; name: string; posterPath: string };

type Event = { title: string; content: string; image: string };

function App() {
	// 외부 url에서 사진 가져오려면 Row에 fetchUrl props 추가해서 넣어주면 됨
	const [movies, setMovies] = useState<Movie[]>(preparedMovie);

	const [events, setEvents] = useState<Event[]>(dummyCards);

	const [homes, setHomes] = useState<Event[]>(dummyHomes);

	const callBackFunction = (cbMoviesList: Movie[]) => {
		console.log(cbMoviesList);
		setMovies(cbMoviesList);
	};

	return (
		<div className="App">
			<BrowserRouter>
				<PrimarySearchAppBar />
				<Route exact path="/">
					<Link to="/write">
						<button type="button">글쓰기</button>
					</Link>
					<Row title="기획전" rowMovies={events} directTo="/prices" />
					<Row title="Start-UP Homes" rowMovies={homes} directTo="/details" />
				</Route>
				<Route path="/details">
					<PostDetail />
				</Route>
				<Route path="/write">
					<WritePage movieList={movies} callBackfunc={callBackFunction} />
				</Route>
				<Route path="/prices">
					<PostPrices />
				</Route>
				<Route path="/pricedetail">
					<PriceDetail />
				</Route>
				<Route path="/createprice">
					<CreatePrice />
				</Route>
			</BrowserRouter>
		</div>
	);
}

export default App;
