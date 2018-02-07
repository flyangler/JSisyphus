package com.slightlyloony.jsisyphus.examples;

import com.slightlyloony.jsisyphus.DrawingContext;
import com.slightlyloony.jsisyphus.Point;
import com.slightlyloony.jsisyphus.TableTriangle;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

/**
 * @author Tom Dilatush  tom@dilatush.com
 */
public class Heart {

    private static final double H = 1;                 // height of midline at scale factor 1...
    private static final double LT = toRadians( 60 );  // interior angle from midline to center of lobe...
    private static final double R = 0.35 * H;          // radius of upper lobes (circular arcs)...
    private static final double LA = 1.05 * PI;        // arc of lobe...
    private static final double BA = 0.35;             // bottom angle from midline...
    private static final double BR = 0.2;              // bottom control point rho...
    private static final double TR = 0.2;              // top control point rho...

    private final HeartDef def;
    private final DrawingContext dc;


    public Heart( final DrawingContext _dc ) {
        def = new HeartDef();
        dc = _dc;
    }


    public void draw( final double _scaleFactor ) {

        // get a scaled heart definition...
        HeartDef shd = def.scale( _scaleFactor );

        // curve up and to the right lobe...
        dc.curveTo( Point.fromRT( shd.br, BA ), Point.fromRT( shd.tr, shd.lr.angleCB() - PI/2 ), shd.lr.verticeA.vectorTo( shd.lr.verticeC ) );

        // now arc up and to the left, to the top center...
        dc.arcTo( shd.lr.verticeC.vectorTo( shd.ur.verticeB ), -LA );

        // now arc down and to the left lobe...
        dc.arcTo( shd.ur.verticeB.vectorTo( shd.ll.verticeC ), -LA );

        // finally down and right to the origin...
        dc.curveTo( Point.fromRT( shd.tr, shd.ll.angleCB() + PI/2 ), Point.fromRT( shd.br, -BA ), shd.ll.verticeC.vectorTo( shd.ll.verticeA ) );

    }


    // Holds the definition of a heart...
    private static class HeartDef {

        TableTriangle ur;  // the upper right lobe...
        TableTriangle ul;  // the upper left lobe...
        TableTriangle lr;  // the lower right lobe...
        TableTriangle ll;  // the lower left lobe...
        double br;         // the rho for the bottom curve control point...
        double tr;         // the rho for the top control point...


        public HeartDef( final TableTriangle _ur, final TableTriangle _ul, final TableTriangle _lr, final TableTriangle _ll, final double _br, final double _tr ) {
            ur = _ur;
            ul = _ul;
            lr = _lr;
            ll = _ll;
            br = _br;
            tr = _tr;
        }


        // constructs a heart at scale factor 1...
        private HeartDef() {

            ur = TableTriangle.fromVVAS( Point.fromXY( 0, 0 ), Point.fromXY( 0, H ), -LT, R );
            double bt = PI - (ur.angleC + (LA - PI));
            lr = TableTriangle.fromVVAS( ur.verticeA, ur.verticeC, -bt, R );
            ul = TableTriangle.fromVVAS( Point.fromXY( 0, 0 ), Point.fromXY( 0, H ), LT, R );
            ll = TableTriangle.fromVVAS( ul.verticeA, ul.verticeC, bt, R );

            br = BR;
            tr = TR;
        }


        private HeartDef scale( final double _scaleFactor ) {

            TableTriangle nur = ur.scaleToA( _scaleFactor );
            TableTriangle nul = ul.scaleToA( _scaleFactor );
            TableTriangle nlr = lr.scaleToA( _scaleFactor );
            TableTriangle nll = ll.scaleToA( _scaleFactor );

            br = BR * _scaleFactor;
            tr = TR * _scaleFactor;

            return new HeartDef( nur, nul, nlr, nll, br, tr );
        }
    }
}