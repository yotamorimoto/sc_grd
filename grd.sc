Grd0 {
    var n;
    var <>xn;
    *new { |size| ^super.new.fill(size?64) }
}

Grd1 : Grd0 {
	fill { |size| n=size; xn=Array.fill(n, { 1.0.rand2 }) }
	rmap { |r| ^r }
	next { |r,g|
		var prev = xn.copy;
		var halfG = g * 0.5;
		var rx = this.rmap(r);
		n.do { |i|
			xn[i] = fold2(
				((1.0 - g) * this.f(rx, prev[i]))
				+
				(halfG * (
					this.f(rx, prev.wrapAt(i+1))
					+
					this.f(rx, prev.wrapAt(i-1))
				))
			);
		};
		^xn;
	}
}
Brown : Grd1 {
	rmap { |r| ^r.linlin(0,1,0.01,1) }
	f { |r,x| ^x + r.rand2 }
}
Logis : Grd1 {
	rmap { |r| ^r.linlin(0,1,1.5,2) }
	f { |r,x| ^1.0 - (r * x.squared) }
}

Grd2 : Grd0 {
	var <>yn;
	fill { |size|
		n  = size;
		xn = Array.fill(n, { 1.0.rand2 });
		yn = Array.fill(n, { 2pi.rand });
	}
	next { |r,g|
		var prex = xn.copy;
		var prey = yn.copy;
		var halfG = g * 0.5;
		var rx = this.rmapx(r);
		var ry = this.rmapy(r);
		n.do { |i|
			xn[i] = fold2(
				(1.0 - g) * this.fx(rx, prex[i], prey[i])
				+
				(halfG * (
					this.fx(rx,prex.wrapAt(i+1),prey.wrapAt(i+1))
					+
					this.fx(rx,prex.wrapAt(i-1),prey.wrapAt(i-1))
				))
			);
			yn[i] = this.fy(ry, prex[i], prey[i]);
		};
		^xn;
	}
}
Fbsin : Grd2 {
	rmapx { |r| ^r.linlin(0,1,1,3) } // fb
	rmapy { |r| ^r.linlin(0,1,1,1.5) }
	fx { |r,x,y|
		^sin(y + (r*x))
	}
	fy { |r,x,y|
		^r * y % 2pi
	}
}