

public function main() {
    worker w1{
      int i = 20;
      if (0 > 1) {
          i -> w2;
      }
    }

    worker w2 {
      int j = 25;
      j = <- w1;
    }
}

