/**
 * Just a bot to beat the default one
 */

var config = {
	2: {
		point: "11",
		orientation: "vertical"
	},
	3: {
		point: "31",
		orientation: "horizontal"
	},
	4: {
		point: "74",
		orientation: "vertical"
	},
	5: {
		point: "07",
		orientation: "horizontal"
	}
};

var input = JSON.parse(process.argv.slice(2));

var moves = [
	"00",
	"01",
	"22",
	"23",
	"24",
	"42",
	"43",
	"44",
	"45",
	"37",
	"47",
	"57",
	"67",
	"77"
];

if (input.cmd == "init") {
	console.log(JSON.stringify(config));
} else {
	var i = 0;
	while (input.hit.indexOf(moves[i]) > -1 || input.missed.indexOf(moves[i]) > -1) {
		i++;
	}
	console.log(JSON.stringify({
		move: moves[i]
	}));
}

process.exit(0);