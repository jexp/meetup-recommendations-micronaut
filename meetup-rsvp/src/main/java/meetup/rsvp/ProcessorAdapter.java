package meetup.rsvp;

import org.reactivestreams.*;
import java.util.function.*;

public class ProcessorAdapter<IN,OUT> implements Processor<IN,OUT> {
    private Subscriber<? super OUT> downstream;
    private Subscription upstream;
    private Function<IN,OUT> transform;

		public ProcessorAdapter(Function<IN,OUT> transform) {
			this.transform = transform;
		}
        @Override
        public void onSubscribe(Subscription subscription) {
	        System.out.println("SUBSCRIPTION: "+subscription);
            this.upstream = subscription;

        }

        @Override
        public void onNext(IN input) {
/* todo callback
//          System.out.println(input);
            if (max-- == 0) {
                upstream.cancel();
            } else {
                // upstream.request(1);
            }
*/
            downstream.onNext(transform.apply(input));
        }

        @Override
        public void onError(Throwable t) {
//            t.printStackTrace();
            upstream.cancel();
            downstream.onError(t);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }

        @Override
        public void subscribe(Subscriber<? super OUT> subscriber) {
            this.downstream = subscriber;
            downstream.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    upstream.request(n);
                }

                @Override
                public void cancel() {
                    upstream.cancel();
                }
            });
        }
    }