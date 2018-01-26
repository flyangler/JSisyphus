package com.slightlyloony.jsisyphus.examples;

import com.slightlyloony.jsisyphus.ATrack;
import com.slightlyloony.jsisyphus.positions.Position;

import java.io.IOException;

/**
 * Draws a series of nested bubbles...
 *
 * @author Tom Dilatush  tom@dilatush.com
 */
public class NestedBubbles extends ATrack {


    public NestedBubbles( final String baseFileName ) {
        super( baseFileName );
    }


    public void trace() throws IOException {

        //if( alreadyTraced() ) return;

        // we start at the outside...
        // dc.eraseToRT( 1, 0 );
        dc.lineToRT( 1, 0 );

        bubble( 1, 3 );

        dc.renderPNG( pngFileName );
        dc.write( trackFileName );
    }


    /**
     * Traces a circle at the given radius, then recurses into the seven enclosed circles if the given nesting state is greater than zero.  On entry, assumes
     * the current position is on the outer circle to be traced and that the (possibly translated) origin is in the center of the outer circle.  On exit,
     * current position is at the same location, and the origin is in the center of the outer circle.
     *
     * @param _radius the radius of the outer circle to be traced.
     * @param _nestingLevel the number of levels to nest bubbles.
     */
    private void bubble( final double _radius, final int _nestingLevel ) {

        // some setup...
        double outerRadius = _radius;
        double innerRadius = outerRadius / 3;

        // first we draw the outer circle...
        dc.arcAroundRT( -outerRadius, 0, Math.PI * 2 );

        // if our nesting level is greater than zero, we recurse into the inner circles...
        if( _nestingLevel > 0 ) {

            // first we draw our six exterior nested circles...
            for( int ic = 0; ic < 6; ic++ ) {

                // draw the circle...
                dc.translateByRT( 2 * innerRadius, 0 );
                if( ic != 0 ) dc.arcAround( Position.CENTER, 2 * Math.PI / 3 );  // get to the top of the circle...
                bubble( innerRadius, _nestingLevel - 1 );

                // now move around to where our next exterior (or interior) circle touches this one...
                dc.arcAround( Position.CENTER, ((ic == 5) ? Math.PI : 2 * Math.PI / 3) );

                // now rotate ourselves for the next exterior circle...
                dc.translateByRT( -2 * innerRadius, 0 );
                dc.rotateBy( Math.PI / 3 );
            }

            // now the center nested circle...
            dc.arcAround( Position.CENTER, Math.PI / 3 );
            bubble( innerRadius, _nestingLevel - 1 );

            // finally, get ourselves back up to top dead center...
            dc.translateByRT( 2 * innerRadius, 0 );
            dc.arcAround( Position.CENTER, Math.PI );
            dc.translateByRT( -2 * innerRadius, 0 );
        }
    }
}